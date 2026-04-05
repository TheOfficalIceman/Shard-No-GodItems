package youss3f.unaux.com.shardascension.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public final class AbilitySwapCommand implements CommandExecutor {

    private final ShardAscensionPlugin plugin;

    public AbilitySwapCommand(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }
        if (!plugin.getAbilityManager().cycleAbility(player)) {
            player.sendMessage("You need two abilities to switch.");
        }
        return true;
    }
}
