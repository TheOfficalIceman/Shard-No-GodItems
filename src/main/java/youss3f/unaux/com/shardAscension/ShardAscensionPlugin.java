package youss3f.unaux.com.shardascension;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import youss3f.unaux.com.shardascension.ability.AbilityManager;
import youss3f.unaux.com.shardascension.command.AbilityCommand;
import youss3f.unaux.com.shardascension.command.AbilitySwapCommand;
import youss3f.unaux.com.shardascension.command.AscensionGiveCommand;
import youss3f.unaux.com.shardascension.command.RollAbilityCommand;
import youss3f.unaux.com.shardascension.command.SetAbilityCommand;
import youss3f.unaux.com.shardascension.command.SetShardsCommand;
import youss3f.unaux.com.shardascension.data.PlayerDataManager;
import youss3f.unaux.com.shardascension.item.ItemManager;
import youss3f.unaux.com.shardascension.listener.PlayerConnectionListener;
import youss3f.unaux.com.shardascension.listener.PlayerItemListener;
import youss3f.unaux.com.shardascension.util.AbilityRegistrar;
import youss3f.unaux.com.shardascension.util.ActionBarUpdater;

public final class ShardAscensionPlugin extends JavaPlugin {

    private PlayerDataManager playerDataManager;
    private ItemManager itemManager;
    private AbilityManager abilityManager;
    private NamespacedKey fragmentKey;
    private NamespacedKey combinationKey;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fragmentKey = new NamespacedKey(this, "fragment_item");
        combinationKey = new NamespacedKey(this, "combination_item");

        playerDataManager = new PlayerDataManager(this);
        itemManager = new ItemManager(this);
        abilityManager = new AbilityManager(this, playerDataManager);

        AbilityRegistrar.registerAll(abilityManager);
        itemManager.registerRecipes();

        registerCommands();
        registerListeners();

        new ActionBarUpdater(this, abilityManager, playerDataManager).runTaskTimer(this, 1L, 2L);
        Bukkit.getOnlinePlayers().forEach(playerDataManager::ensureLoaded);
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.saveAll();
        }
    }

    private void registerCommands() {
        registerCommand("rollability", new RollAbilityCommand(this));
        registerCommand("ability", new AbilityCommand(this));
        registerCommand("abilityswap", new AbilitySwapCommand(this));
        registerCommand("ascensiongive", new AscensionGiveCommand(this));
        registerCommand("setability", new SetAbilityCommand(this));
        registerCommand("setshards", new SetShardsCommand(this));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerItemListener(this), this);
    }

    private void registerCommand(String name, Object executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalStateException("Missing command declaration for " + name);
        }
        if (executor instanceof org.bukkit.command.CommandExecutor commandExecutor) {
            command.setExecutor(commandExecutor);
        }
        if (executor instanceof org.bukkit.command.TabCompleter tabCompleter) {
            command.setTabCompleter(tabCompleter);
        }
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public NamespacedKey getFragmentKey() {
        return fragmentKey;
    }

    public NamespacedKey getCombinationKey() {
        return combinationKey;
    }
}
