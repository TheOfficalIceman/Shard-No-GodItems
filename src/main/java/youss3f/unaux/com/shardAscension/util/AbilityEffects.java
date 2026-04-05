package youss3f.unaux.com.shardascension.util;

import java.util.Collection;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public final class AbilityEffects {

    private AbilityEffects() {
    }

    public static void ring(ShardAscensionPlugin plugin, Location center, Particle particle, double radius, int points) {
        if (!plugin.getConfig().getBoolean("ui.particles-enabled", true) || center.getWorld() == null) {
            return;
        }
        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2D * i) / points;
            Location point = center.clone().add(Math.cos(angle) * radius, 0.2, Math.sin(angle) * radius);
            center.getWorld().spawnParticle(particle, point, 1, 0, 0, 0, 0);
        }
    }

    public static void dustBurst(ShardAscensionPlugin plugin, Location center, Color color, int count) {
        if (!plugin.getConfig().getBoolean("ui.particles-enabled", true) || center.getWorld() == null) {
            return;
        }
        center.getWorld().spawnParticle(Particle.DUST, center, count, 0.6, 0.8, 0.6, new Particle.DustOptions(color, 1.5f));
    }

    public static void line(ShardAscensionPlugin plugin, Location start, Vector direction, double length, Particle particle) {
        if (!plugin.getConfig().getBoolean("ui.particles-enabled", true) || start.getWorld() == null) {
            return;
        }
        Vector normalized = direction.clone().normalize().multiply(0.5);
        Location cursor = start.clone();
        for (double traveled = 0; traveled < length; traveled += 0.5) {
            cursor.add(normalized);
            start.getWorld().spawnParticle(particle, cursor, 1, 0, 0, 0, 0);
        }
    }

    public static void pushOut(Collection<? extends LivingEntity> targets, Location origin, double horizontalStrength, double verticalBoost) {
        for (LivingEntity target : targets) {
            Vector velocity = target.getLocation().toVector().subtract(origin.toVector()).normalize().multiply(horizontalStrength);
            velocity.setY(verticalBoost);
            target.setVelocity(velocity);
        }
    }

    public static void pullIn(Collection<? extends LivingEntity> targets, Location origin, double strength) {
        for (LivingEntity target : targets) {
            Vector velocity = origin.toVector().subtract(target.getLocation().toVector()).normalize().multiply(strength);
            velocity.setY(Math.max(0.05, velocity.getY() + 0.1));
            target.setVelocity(target.getVelocity().add(velocity));
        }
    }

    public static void buff(Player player, PotionEffectType type, int durationTicks, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, durationTicks, amplifier, true, true, true));
    }

    public static void sound(ShardAscensionPlugin plugin, Player player, Sound sound, float volume, float pitch) {
        if (plugin.getConfig().getBoolean("ui.sounds-enabled", true)) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
