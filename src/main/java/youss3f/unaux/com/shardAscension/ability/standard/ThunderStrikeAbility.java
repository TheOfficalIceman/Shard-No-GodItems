package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;

public final class ThunderStrikeAbility extends AbstractAbility {
    public ThunderStrikeAbility() { super("thunder_strike", "Thunder Strike", NamedTextColor.YELLOW, "⚡", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        Location hit = player.rayTraceBlocks(number(plugin, "range", 18), FluidCollisionMode.NEVER) != null
            ? player.rayTraceBlocks(number(plugin, "range", 18), FluidCollisionMode.NEVER).getHitPosition().toLocation(player.getWorld())
            : player.getLocation().add(player.getLocation().getDirection().normalize().multiply(6));
        player.getWorld().strikeLightningEffect(hit);
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, hit, 40, 0.5, 1, 0.5, 0.2);
        player.getWorld().getNearbyLivingEntities(hit, 2.5).forEach(target -> target.damage(number(plugin, "damage", 7), player));
        sound(plugin, hit, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
    }
}
