package youss3f.unaux.com.shardascension.ability.standard;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class EarthSlamAbility extends AbstractAbility {
    public EarthSlamAbility() { super("earth_slam", "Earth Slam", NamedTextColor.DARK_GREEN, "⬟", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        player.setVelocity(player.getVelocity().setY(1.0));
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                if (isStandingOnSolidGround(player)) {
                    double radius = number(plugin, "radius", 5);
                    double damage = number(plugin, "damage", 7);
                    List<LivingEntity> targets = nearbyTargets(player, radius);
                    targets.forEach(target -> damage(player, target, damage));
                    AbilityEffects.pushOut(targets, player.getLocation(), 1.2, 0.4);
                    particles(plugin, player.getLocation(), Particle.BLOCK, 40, 0.8, 0.2, 0.8, 0.05);
                    sound(plugin, player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 0.8f);
                    cancel();
                }
            }
        };
        BukkitTask scheduled = task.runTaskTimer(plugin, 2L, 2L);
        plugin.getAbilityManager().bindActiveTask(player.getUniqueId(), scheduled);
    }
}
