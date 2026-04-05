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

public final class FlameWaveAbility extends AbstractAbility {
    public FlameWaveAbility() { super("flame_wave", "Flame Wave", NamedTextColor.GOLD, "🔥", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        double radius = number(plugin, "radius", 5);
        double damage = number(plugin, "damage", 6);
        int fireTicks = integer(plugin, "fire-ticks", 100);
        List<LivingEntity> targets = nearbyTargets(player, radius);
        for (LivingEntity target : targets) {
            damage(player, target, damage);
            target.setFireTicks(fireTicks);
        }
        AbilityEffects.line(plugin, player.getLocation(), player.getLocation().getDirection(), radius + 2, Particle.FLAME);
        particles(plugin, player.getLocation(), Particle.LAVA, 20, 0.5, 0.3, 0.5, 0.02);
        sound(plugin, player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1f, 0.9f);
    }
}
