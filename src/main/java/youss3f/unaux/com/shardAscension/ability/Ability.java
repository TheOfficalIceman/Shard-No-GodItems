package youss3f.unaux.com.shardascension.ability;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public interface Ability {

    String getId();

    Component getDisplayName();

    Component getIcon();

    boolean isCombined();

    default String getConfigPath() {
        return "abilities." + getId();
    }

    void activate(ShardAscensionPlugin plugin, Player player);
}
