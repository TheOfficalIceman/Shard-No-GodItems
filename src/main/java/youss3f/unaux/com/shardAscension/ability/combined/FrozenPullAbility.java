package youss3f.unaux.com.shardascension.ability.combined;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class FrozenPullAbility extends AbstractAbility {
    public FrozenPullAbility() { super("frozen_pull", "Frozen Pull", NamedTextColor.AQUA, "❅", true); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        Location center = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(3));
        BukkitRunnable task = new BukkitRunnable() {
            int lived = 0;
            @Override
            public void run() {
                if (lived++ >= integer(plugin, "duration-ticks", 90) || !player.isOnline()) {
                    cancel();
                    return;
                }
                for (LivingEntity target : player.getWorld().getNearbyLivingEntities(center, number(plugin, "radius", 6))) {
                    if (target == player) {
                        continue;
                    }
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 12, 3, true, true, true));
                    target.setFreezeTicks(Math.max(target.getFreezeTicks(), 25));
                }
                AbilityEffects.pullIn(player.getWorld().getNearbyLivingEntities(center, number(plugin, "radius", 6)), center, number(plugin, "force", 0.3));
                particles(plugin, center, Particle.SNOWFLAKE, 20, 0.6, 0.8, 0.6, 0.04);
                if (lived % 10 == 0) {
                    sound(plugin, center, Sound.BLOCK_POWDER_SNOW_PLACE, 0.8f, 1.2f);
                }
            }
        };
        BukkitTask scheduled = task.runTaskTimer(plugin, 0L, 1L);
        plugin.getAbilityManager().bindActiveTask(player.getUniqueId(), scheduled);
    }
}
