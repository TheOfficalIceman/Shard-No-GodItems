package youss3f.unaux.com.shardascension.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public final class RollAbilityCommand implements CommandExecutor {

    private final ShardAscensionPlugin plugin;

    public RollAbilityCommand(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }
        if (!plugin.getAbilityManager().grantRandomAbility(player, false)) {
            player.sendMessage("You cannot roll another ability.");
        }
        return true;
    }
}
