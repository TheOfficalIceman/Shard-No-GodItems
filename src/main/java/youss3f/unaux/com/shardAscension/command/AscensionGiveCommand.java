package youss3f.unaux.com.shardascension.command;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public final class AscensionGiveCommand implements CommandExecutor, TabCompleter {

    private final ShardAscensionPlugin plugin;

    public AscensionGiveCommand(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("/ascensiongive <player> <amount>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage("Amount must be a number.");
            return true;
        }
        target.getInventory().addItem(plugin.getItemManager().createFragment(amount));
        sender.sendMessage("Fragments sent.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return List.of();
    }
}
