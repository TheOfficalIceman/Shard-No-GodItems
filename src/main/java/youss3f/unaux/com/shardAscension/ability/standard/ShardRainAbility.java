package youss3f.unaux.com.shardascension.ability.standard;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;

public final class ShardRainAbility extends AbstractAbility {
    public ShardRainAbility() { super("shard_rain", "Shard Rain", NamedTextColor.BLUE, "✵", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        int waves = integer(plugin, "waves", 4);
        double radius = number(plugin, "radius", 5);
        double damage = number(plugin, "damage", 4);
        BukkitRunnable task = new BukkitRunnable() {
            int tick = 0;
            @Override
            public void run() {
                if (tick++ >= waves || !player.isOnline()) {
                    cancel();
                    return;
                }
                Location center = player.getLocation();
                particles(plugin, center.clone().add(0, 5, 0), Particle.FALLING_DUST, 40, radius, 0.1, radius, 0.01);
                for (LivingEntity target : nearbyTargets(player, radius)) {
                    damage(player, target, damage);
                    particles(plugin, target.getLocation().add(0, 1, 0), Particle.CRIT, 10, 0.2, 0.4, 0.2, 0.1);
                }
                sound(plugin, center, Sound.BLOCK_AMETHYST_CLUSTER_FALL, 0.8f, 1.2f);
            }
        };
        BukkitTask scheduled = task.runTaskTimer(plugin, 0L, 10L);
        plugin.getAbilityManager().bindActiveTask(player.getUniqueId(), scheduled);
    }
}
