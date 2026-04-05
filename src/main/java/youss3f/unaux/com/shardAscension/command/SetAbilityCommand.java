package youss3f.unaux.com.shardascension.command;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.Ability;
import youss3f.unaux.com.shardascension.data.PlayerAbilityData;

public final class SetAbilityCommand implements CommandExecutor, TabCompleter {

    private final ShardAscensionPlugin plugin;

    public SetAbilityCommand(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("shardascension.admin")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage("/setability <player> <ability>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }
        String abilityId = args[1];
        Ability ability = plugin.getAbilityManager().getAbility(abilityId);
        if (ability == null) {
            sender.sendMessage("Unknown ability: " + abilityId);
            return true;
        }
        PlayerAbilityData data = plugin.getPlayerDataManager().ensureLoaded(target);
        data.getAbilityIds().clear();
        data.getAbilityIds().add(abilityId);
        data.setSelectedAbilityId(abilityId);
        plugin.getPlayerDataManager().save(data);
        target.sendMessage("Your ability has been set to: " + ability.getId());
        sender.sendMessage("Set " + target.getName() + "'s ability to: " + ability.getId());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        if (args.length == 2) {
            return plugin.getAbilityManager().completeAbilityIds(args[1]);
        }
        return List.of();
    }
}
