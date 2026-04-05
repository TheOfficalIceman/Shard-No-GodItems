package youss3f.unaux.com.shardascension.ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.data.PlayerAbilityData;
import youss3f.unaux.com.shardascension.data.PlayerDataManager;

public final class AbilityManager {

    private final ShardAscensionPlugin plugin;
    private final PlayerDataManager playerDataManager;
    private final Map<String, Ability> abilities = new LinkedHashMap<>();
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();
    private final Map<Set<String>, String> combinations = new HashMap<>();
    private final Map<UUID, BukkitTask> activeTasks = new ConcurrentHashMap<>();

    public AbilityManager(ShardAscensionPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    public void registerAbility(Ability ability) {
        abilities.put(ability.getId(), ability);
    }

    public void registerCombination(String first, String second, String result) {
        combinations.put(Set.of(first, second), result);
    }

    public Ability getAbility(String id) {
        return abilities.get(id);
    }

    public boolean grantRandomAbility(Player player, boolean firstJoin) {
        PlayerAbilityData data = playerDataManager.ensureLoaded(player);
        int maxAbilities = plugin.getConfig().getInt("max-abilities", 2);
        if (data.getAbilityIds().size() >= maxAbilities) {
            return false;
        }
        List<Ability> candidates = abilities.values().stream()
            .filter(ability -> !ability.isCombined())
            .filter(ability -> plugin.getConfig().getBoolean(ability.getConfigPath() + ".enabled", true))
            .filter(ability -> !data.getAbilityIds().contains(ability.getId()))
            .toList();
        if (candidates.isEmpty()) {
            return false;
        }
        Ability selected = candidates.get((int) (Math.random() * candidates.size()));
        grantAbility(player, selected.getId());
        showRollFeedback(player, selected, firstJoin);
        return true;
    }

    public void grantAbility(Player player, String abilityId) {
        PlayerAbilityData data = playerDataManager.ensureLoaded(player);
        if (data.getAbilityIds().contains(abilityId)) {
            return;
        }
        data.getAbilityIds().add(abilityId);
        if (data.getSelectedAbilityId() == null) {
            data.setSelectedAbilityId(abilityId);
        }
        playerDataManager.save(data);
    }

    public Ability getSelectedAbility(Player player) {
        PlayerAbilityData data = playerDataManager.ensureLoaded(player);
        String selectedId = data.getSelectedAbilityId();
        if (selectedId == null && !data.getAbilityIds().isEmpty()) {
            selectedId = data.getAbilityIds().get(0);
            data.setSelectedAbilityId(selectedId);
        }
        return selectedId == null ? null : getAbility(selectedId);
    }

    public boolean useSelectedAbility(Player player) {
        Ability ability = getSelectedAbility(player);
        if (ability == null) {
            player.sendMessage(Component.text("You do not have an ability selected.", NamedTextColor.RED));
            return false;
        }
        if (!plugin.getConfig().getBoolean(ability.getConfigPath() + ".enabled", true)) {
            player.sendMessage(Component.text("That ability is disabled.", NamedTextColor.RED));
            return false;
        }
        long remaining = getRemainingCooldownMillis(player.getUniqueId(), ability.getId());
        if (remaining > 0L) {
            player.sendMessage(Component.text("Ability cooldown: " + formatCooldown(remaining), NamedTextColor.RED));
            return false;
        }
        cancelActiveTask(player.getUniqueId());
        ability.activate(plugin, player);
        setCooldown(player.getUniqueId(), ability.getId(), getCooldownMillis(ability));
        return true;
    }

    public boolean cycleAbility(Player player) {
        PlayerAbilityData data = playerDataManager.ensureLoaded(player);
        if (data.getAbilityIds().size() < 2) {
            return false;
        }
        int currentIndex = Math.max(0, data.getAbilityIds().indexOf(data.getSelectedAbilityId()));
        String next = data.getAbilityIds().get((currentIndex + 1) % data.getAbilityIds().size());
        data.setSelectedAbilityId(next);
        playerDataManager.save(data);
        Ability ability = getAbility(next);
        if (ability != null) {
            player.sendMessage(Component.text("Selected ability: ", NamedTextColor.GRAY).append(ability.getDisplayName()));
        }
        return true;
    }

    public boolean combine(Player player, ItemStack stack) {
        PlayerAbilityData data = playerDataManager.ensureLoaded(player);
        if (data.getAbilityIds().size() != 2) {
            player.sendMessage(Component.text("You need exactly 2 abilities to combine.", NamedTextColor.RED));
            return false;
        }
        String resultId = combinations.get(Set.copyOf(data.getAbilityIds()));
        if (resultId == null) {
            player.sendMessage(Component.text("Those abilities cannot be combined.", NamedTextColor.RED));
            return false;
        }
        Ability combined = getAbility(resultId);
        if (combined == null) {
            return false;
        }
        data.getAbilityIds().clear();
        data.getAbilityIds().add(resultId);
        data.setSelectedAbilityId(resultId);
        playerDataManager.save(data);
        stack.subtract(1);
        player.showTitle(Title.title(Component.text("Ascension", NamedTextColor.LIGHT_PURPLE), Component.text("Combined into ").append(combined.getDisplayName())));
        player.spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1, 0), 40, 0.5, 0.5, 0.5, 0.04);
        player.spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, 1, 0), 30, 0.5, 0.6, 0.5, 0.02);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.15f);
        return true;
    }

    public long getRemainingCooldownMillis(UUID uuid, String abilityId) {
        long endsAt = cooldowns.getOrDefault(uuid, Map.of()).getOrDefault(abilityId, 0L);
        return Math.max(0L, endsAt - System.currentTimeMillis());
    }

    public long getCooldownMillis(Ability ability) {
        return (long) (plugin.getConfig().getDouble(ability.getConfigPath() + ".cooldown", 10.0D) * 1000L);
    }

    public String formatCooldown(long cooldownMillis) {
        return String.format("%.1fs", cooldownMillis / 1000.0D);
    }

    public Component buildActionBar(Player player) {
        Ability ability = getSelectedAbility(player);
        if (ability == null) {
            return Component.text("[No Ability]", NamedTextColor.DARK_GRAY);
        }
        long remaining = getRemainingCooldownMillis(player.getUniqueId(), ability.getId());
        Component status = remaining > 0
            ? Component.text(formatCooldown(remaining), NamedTextColor.RED)
            : Component.text("Ready", NamedTextColor.GREEN);
        return Component.join(
            JoinConfiguration.noSeparators(),
            ability.getIcon(),
            Component.text(" [", NamedTextColor.DARK_GRAY),
            ability.getDisplayName(),
            Component.text("] - ", NamedTextColor.DARK_GRAY),
            status
        );
    }

    public List<String> completeAbilityIds(String input) {
        List<String> matches = new ArrayList<>();
        StringUtil.copyPartialMatches(input, abilities.keySet(), matches);
        return matches;
    }

    public void bindActiveTask(UUID uuid, BukkitTask task) {
        cancelActiveTask(uuid);
        activeTasks.put(uuid, task);
    }

    public void cancelActiveTask(UUID uuid) {
        BukkitTask task = activeTasks.remove(uuid);
        if (task != null) {
            task.cancel();
        }
    }

    private void setCooldown(UUID uuid, String abilityId, long durationMillis) {
        cooldowns.computeIfAbsent(uuid, ignored -> new ConcurrentHashMap<>()).put(abilityId, System.currentTimeMillis() + durationMillis);
    }

    private void showRollFeedback(Player player, Ability ability, boolean firstJoin) {
        player.sendMessage(Component.text(firstJoin ? "First ability unlocked: " : "You rolled: ", NamedTextColor.GOLD).append(ability.getDisplayName()));
        player.showTitle(Title.title(Component.text("Ability Unlocked", NamedTextColor.AQUA), ability.getDisplayName()));
        player.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, player.getLocation().add(0, 1, 0), 35, 0.5, 0.5, 0.5, 0.01);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.3f);
    }
}
