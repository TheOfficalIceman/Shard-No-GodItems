package youss3f.unaux.com.shardascension.ability.combined;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class FlameBarrageAbility extends AbstractAbility {
    public FlameBarrageAbility() { super("flame_barrage", "Flame Barrage", NamedTextColor.RED, "✷", true); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        BukkitRunnable task = new BukkitRunnable() {
            int shots = 0;
            @Override
            public void run() {
                if (shots++ >= integer(plugin, "shots", 8) || !player.isOnline()) {
                    cancel();
                    return;
                }
                AbilityEffects.line(plugin, player.getEyeLocation(), player.getEyeLocation().getDirection(), 20, Particle.FLAME);
                RayTraceResult entityHit = player.rayTraceEntities(20);
                RayTraceResult blockHit = player.rayTraceBlocks(20, FluidCollisionMode.NEVER);
                if (entityHit != null && entityHit.getHitEntity() instanceof LivingEntity living) {
                    damage(player, living, number(plugin, "damage", 4.5));
                    living.setFireTicks(integer(plugin, "fire-ticks", 120));
                    particles(plugin, living.getLocation().add(0, 1, 0), Particle.LAVA, 8, 0.2, 0.2, 0.2, 0.04);
                } else if (blockHit != null) {
                    particles(plugin, blockHit.getHitPosition().toLocation(player.getWorld()), Particle.FLAME, 6, 0.2, 0.2, 0.2, 0.02);
                }
                sound(plugin, player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.7f, 1.3f);
            }
        };
        BukkitTask scheduled = task.runTaskTimer(plugin, 0L, 2L);
        plugin.getAbilityManager().bindActiveTask(player.getUniqueId(), scheduled);
    }
}
