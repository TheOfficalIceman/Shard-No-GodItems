package youss3f.unaux.com.shardascension.util;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbilityManager;
import youss3f.unaux.com.shardascension.data.PlayerDataManager;

public final class ActionBarUpdater extends BukkitRunnable {

    private final ShardAscensionPlugin plugin;
    private final AbilityManager abilityManager;
    private final PlayerDataManager playerDataManager;

    public ActionBarUpdater(ShardAscensionPlugin plugin, AbilityManager abilityManager, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.abilityManager = abilityManager;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            playerDataManager.ensureLoaded(player);
            player.sendActionBar(abilityManager.buildActionBar(player));
        }
    }
}
