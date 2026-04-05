package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class DashAbility extends AbstractAbility {
    public DashAbility() { super("dash", "Dash", NamedTextColor.AQUA, "✦", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        double strength = number(plugin, "strength", 2.1);
        player.setVelocity(player.getLocation().getDirection().normalize().multiply(strength).setY(0.18));
        particles(plugin, player.getLocation(), Particle.CLOUD, 22, 0.3, 0.2, 0.3, 0.02);
        AbilityEffects.line(plugin, player.getLocation(), player.getLocation().getDirection(), 4, Particle.END_ROD);
        sound(plugin, player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1f, 1.2f);
    }
}
