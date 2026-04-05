package youss3f.unaux.com.shardascension.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.data.PlayerAbilityData;

public final class PlayerConnectionListener implements Listener {

    private final ShardAscensionPlugin plugin;

    public PlayerConnectionListener(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerAbilityData data = plugin.getPlayerDataManager().ensureLoaded(event.getPlayer());
        if (data.getAbilityIds().isEmpty()) {
            plugin.getAbilityManager().grantRandomAbility(event.getPlayer(), true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getAbilityManager().cancelActiveTask(event.getPlayer().getUniqueId());
        plugin.getPlayerDataManager().save(plugin.getPlayerDataManager().ensureLoaded(event.getPlayer()));
    }
}
