package youss3f.unaux.com.shardascension.ability.standard;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class ShockwaveAbility extends AbstractAbility {
    public ShockwaveAbility() { super("shockwave", "Shockwave", NamedTextColor.GOLD, "✹", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        double radius = number(plugin, "radius", 5);
        double damage = number(plugin, "damage", 5);
        double knockback = number(plugin, "knockback", 1.8);
        List<LivingEntity> targets = nearbyTargets(player, radius);
        targets.forEach(target -> damage(player, target, damage));
        AbilityEffects.pushOut(targets, player.getLocation(), knockback, 0.35);
        AbilityEffects.ring(plugin, player.getLocation(), Particle.SONIC_BOOM, radius, 24);
        sound(plugin, player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.9f, 1.3f);
    }
}
