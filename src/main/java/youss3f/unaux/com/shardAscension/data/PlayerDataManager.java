package youss3f.unaux.com.shardascension.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public final class PlayerDataManager {

    private final ShardAscensionPlugin plugin;
    private final Map<UUID, PlayerAbilityData> cache = new ConcurrentHashMap<>();
    private final File dataFile;
    private final YamlConfiguration configuration;

    public PlayerDataManager(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException exception) {
                throw new IllegalStateException("Could not create playerdata.yml", exception);
            }
        }
        this.configuration = YamlConfiguration.loadConfiguration(dataFile);
    }

    public PlayerAbilityData ensureLoaded(Player player) {
        return ensureLoaded(player.getUniqueId());
    }

    public PlayerAbilityData ensureLoaded(UUID uuid) {
        return cache.computeIfAbsent(uuid, key -> {
            ConfigurationSection section = configuration.getConfigurationSection("players." + key);
            ArrayList<String> abilities = section != null ? new ArrayList<>(section.getStringList("abilities")) : new ArrayList<>();
            String selected = section != null ? section.getString("selected") : null;
            int shards = section != null ? section.getInt("shards", 0) : 0;
            if (selected == null && !abilities.isEmpty()) {
                selected = abilities.get(0);
            }
            return new PlayerAbilityData(key, abilities, selected, shards);
        });
    }

    public void save(PlayerAbilityData data) {
        String path = "players." + data.getUuid();
        configuration.set(path + ".abilities", new ArrayList<>(data.getAbilityIds()));
        configuration.set(path + ".selected", data.getSelectedAbilityId());
        configuration.set(path + ".shards", data.getShards());
        try {
            configuration.save(dataFile);
        } catch (IOException exception) {
            plugin.getLogger().severe("Failed to save player data for " + data.getUuid());
        }
    }

    public void saveAll() {
        cache.values().forEach(this::save);
    }
}
