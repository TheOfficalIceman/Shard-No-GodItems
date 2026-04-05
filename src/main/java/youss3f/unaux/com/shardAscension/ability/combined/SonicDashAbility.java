package youss3f.unaux.com.shardascension.ability.combined;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;

public final class SonicDashAbility extends AbstractAbility {
    public SonicDashAbility() { super("sonic_dash", "Sonic Dash", NamedTextColor.AQUA, "✸", true); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        player.setVelocity(player.getLocation().getDirection().normalize().multiply(number(plugin, "strength", 3.2)).setY(0.22));
        List<LivingEntity> targets = nearbyTargets(player, 3);
        targets.forEach(target -> damage(player, target, number(plugin, "damage", 6)));
        particles(plugin, player.getLocation(), Particle.SONIC_BOOM, 1, 0, 0, 0, 0);
        particles(plugin, player.getLocation(), Particle.CLOUD, 28, 0.4, 0.3, 0.4, 0.08);
        sound(plugin, player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.8f, 1.8f);
    }
}
