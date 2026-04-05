package youss3f.unaux.com.shardascension.command;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.data.PlayerAbilityData;

public final class SetShardsCommand implements CommandExecutor, TabCompleter {

    private static final int MAX_SHARDS = 3;
    private final ShardAscensionPlugin plugin;

    public SetShardsCommand(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("shardascension.admin")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage("/setshards <player> <amount>");
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
        if (amount < 0) {
            sender.sendMessage("Amount must be 0 or greater.");
            return true;
        }
        PlayerAbilityData data = plugin.getPlayerDataManager().ensureLoaded(target);
        int newTotal = Math.min(data.getShards() + amount, MAX_SHARDS);
        data.setShards(newTotal);
        plugin.getPlayerDataManager().save(data);
        target.sendMessage("Your shards have been updated: " + newTotal + "/" + MAX_SHARDS);
        sender.sendMessage("Set " + target.getName() + "'s shards to: " + newTotal + "/" + MAX_SHARDS);
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
