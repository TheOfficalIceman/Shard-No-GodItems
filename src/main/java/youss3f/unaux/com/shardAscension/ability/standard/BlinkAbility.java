package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;

public final class BlinkAbility extends AbstractAbility {
    public BlinkAbility() { super("blink", "Blink", NamedTextColor.LIGHT_PURPLE, "✧", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        int range = integer(plugin, "range", 8);
        Location target = player.getLocation().clone();
        for (int i = 1; i <= range; i++) {
            Location candidate = player.getLocation().clone().add(player.getLocation().getDirection().normalize().multiply(i));
            if (candidate.getBlock().isSolid()) {
                break;
            }
            target = candidate;
        }
        particles(plugin, player.getLocation(), Particle.PORTAL, 25, 0.4, 0.8, 0.4, 0.2);
        player.teleport(target);
        particles(plugin, player.getLocation(), Particle.WITCH, 30, 0.4, 0.8, 0.4, 0.1);
        sound(plugin, player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.15f);
        player.sendMessage(Component.text("Blink executed.", NamedTextColor.LIGHT_PURPLE));
    }
}
