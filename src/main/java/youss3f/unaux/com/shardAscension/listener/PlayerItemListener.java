package youss3f.unaux.com.shardascension.listener;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public final class PlayerItemListener implements Listener {

    private final ShardAscensionPlugin plugin;

    public PlayerItemListener(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack item = event.getItem();
        if (!plugin.getItemManager().isCombinationItem(item)) {
            return;
        }
        event.setCancelled(true);
        plugin.getAbilityManager().combine(event.getPlayer(), item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            return;
        }
        if (plugin.getAbilityManager().cycleAbility(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!plugin.getConfig().getBoolean("keep-fragments-on-death", true)) {
            return;
        }
        List<ItemStack> kept = new ArrayList<>();
        event.getDrops().removeIf(drop -> {
            boolean fragment = plugin.getItemManager().isFragment(drop);
            if (fragment) {
                kept.add(drop.clone());
            }
            return fragment;
        });
        event.getItemsToKeep().addAll(kept);
    }
}
