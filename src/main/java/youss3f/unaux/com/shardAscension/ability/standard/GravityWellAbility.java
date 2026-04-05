package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class GravityWellAbility extends AbstractAbility {
    public GravityWellAbility() { super("gravity_well", "Gravity Well", NamedTextColor.DARK_PURPLE, "◎", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        Location center = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(3));
        BukkitRunnable task = new BukkitRunnable() {
            int lived = 0;
            @Override
            public void run() {
                if (lived++ >= integer(plugin, "duration-ticks", 70) || !player.isOnline()) {
                    cancel();
                    return;
                }
                AbilityEffects.pullIn(player.getWorld().getNearbyLivingEntities(center, number(plugin, "radius", 7)), center, number(plugin, "force", 0.24));
                particles(plugin, center, Particle.PORTAL, 20, 0.6, 0.8, 0.6, 0.1);
                if (lived % 10 == 0) {
                    sound(plugin, center, Sound.BLOCK_BEACON_AMBIENT, 0.5f, 0.5f);
                }
            }
        };
        BukkitTask scheduled = task.runTaskTimer(plugin, 0L, 1L);
        plugin.getAbilityManager().bindActiveTask(player.getUniqueId(), scheduled);
    }
}
