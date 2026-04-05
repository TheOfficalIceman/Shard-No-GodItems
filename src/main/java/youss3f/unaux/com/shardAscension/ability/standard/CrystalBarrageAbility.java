package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class CrystalBarrageAbility extends AbstractAbility {
    public CrystalBarrageAbility() { super("crystal_barrage", "Crystal Barrage", NamedTextColor.LIGHT_PURPLE, "❂", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        int shots = integer(plugin, "shots", 6);
        int range = integer(plugin, "range", 18);
        BukkitRunnable task = new BukkitRunnable() {
            int fired = 0;
            @Override
            public void run() {
                if (fired++ >= shots || !player.isOnline()) {
                    cancel();
                    return;
                }
                Location start = player.getEyeLocation();
                AbilityEffects.line(plugin, start, start.getDirection(), range, Particle.END_ROD);
                RayTraceResult entityHit = player.rayTraceEntities(range);
                RayTraceResult blockHit = player.rayTraceBlocks(range, FluidCollisionMode.NEVER);
                if (entityHit != null && entityHit.getHitEntity() instanceof org.bukkit.entity.LivingEntity living) {
                    damage(player, living, number(plugin, "damage", 3.5));
                    particles(plugin, living.getLocation().add(0, 1, 0), Particle.CRIT, 8, 0.2, 0.3, 0.2, 0.08);
                } else if (blockHit != null) {
                    particles(plugin, blockHit.getHitPosition().toLocation(player.getWorld()), Particle.WAX_OFF, 10, 0.2, 0.2, 0.2, 0.02);
                }
                sound(plugin, player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_STEP, 0.8f, 1.8f);
            }
        };
        BukkitTask scheduled = task.runTaskTimer(plugin, 0L, 3L);
        plugin.getAbilityManager().bindActiveTask(player.getUniqueId(), scheduled);
    }
}
