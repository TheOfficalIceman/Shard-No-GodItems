package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;

public final class RadiantHealAbility extends AbstractAbility {
    public RadiantHealAbility() { super("radiant_heal", "Radiant Heal", NamedTextColor.YELLOW, "✚", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        double heal = number(plugin, "heal", 6);
        player.getWorld().getNearbyPlayers(player.getLocation(), number(plugin, "radius", 6)).forEach(target ->
            target.setHealth(Math.min(maxHealth(target), target.getHealth() + heal))
        );
        player.setHealth(Math.min(maxHealth(player), player.getHealth() + heal));
        particles(plugin, player.getLocation(), Particle.HAPPY_VILLAGER, 40, 0.6, 0.8, 0.6, 0.08);
        sound(plugin, player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.8f, 1.4f);
    }
}
