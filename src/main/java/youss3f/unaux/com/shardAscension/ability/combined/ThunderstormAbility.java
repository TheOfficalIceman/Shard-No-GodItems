package youss3f.unaux.com.shardascension.ability.combined;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;

public final class ThunderstormAbility extends AbstractAbility {
    public ThunderstormAbility() { super("thunderstorm", "Thunderstorm", NamedTextColor.YELLOW, "⛈", true); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        Location center = player.getLocation();
        BukkitRunnable task = new BukkitRunnable() {
            int strikes = 0;
            @Override
            public void run() {
                if (strikes++ >= integer(plugin, "strikes", 5) || !player.isOnline()) {
                    cancel();
                    return;
                }
                double radius = number(plugin, "radius", 6);
                Location strike = center.clone().add((Math.random() - 0.5) * radius * 2, 0, (Math.random() - 0.5) * radius * 2);
                player.getWorld().strikeLightningEffect(strike);
                player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, strike, 30, 0.5, 1, 0.5, 0.2);
                player.getWorld().getNearbyLivingEntities(strike, 2.5).forEach(target -> target.damage(number(plugin, "damage", 6), player));
                sound(plugin, strike, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.8f, 1.1f);
            }
        };
        BukkitTask scheduled = task.runTaskTimer(plugin, 0L, 8L);
        plugin.getAbilityManager().bindActiveTask(player.getUniqueId(), scheduled);
    }
}
