package youss3f.unaux.com.shardascension.ability;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public abstract class AbstractAbility implements Ability {

    private final String id;
    private final Component displayName;
    private final Component icon;
    private final boolean combined;

    protected AbstractAbility(String id, String displayName, NamedTextColor color, String icon, boolean combined) {
        this.id = id;
        this.displayName = Component.text(displayName, color).decoration(TextDecoration.ITALIC, false);
        this.icon = Component.text(icon, color).decoration(TextDecoration.ITALIC, false);
        this.combined = combined;
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final Component getDisplayName() {
        return displayName;
    }

    @Override
    public final Component getIcon() {
        return icon;
    }

    @Override
    public final boolean isCombined() {
        return combined;
    }

    protected double number(ShardAscensionPlugin plugin, String key, double fallback) {
        return plugin.getConfig().getDouble(getConfigPath() + "." + key, fallback);
    }

    protected int integer(ShardAscensionPlugin plugin, String key, int fallback) {
        return plugin.getConfig().getInt(getConfigPath() + "." + key, fallback);
    }

    protected void particles(ShardAscensionPlugin plugin, Location location, Particle particle, int count, double x, double y, double z, double extra) {
        if (!plugin.getConfig().getBoolean("ui.particles-enabled", true)) {
            return;
        }
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(particle, location, count, x, y, z, extra);
        }
    }

    protected void sound(ShardAscensionPlugin plugin, Location location, Sound sound, float volume, float pitch) {
        if (!plugin.getConfig().getBoolean("ui.sounds-enabled", true)) {
            return;
        }
        World world = location.getWorld();
        if (world != null) {
            world.playSound(location, sound, volume, pitch);
        }
    }

    protected List<LivingEntity> nearbyTargets(Player player, double radius) {
        List<LivingEntity> entities = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity living && living != player) {
                entities.add(living);
            }
        }
        return entities;
    }

    protected LivingEntity nearestTarget(Player player, double radius) {
        return nearbyTargets(player, radius).stream()
            .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(player.getLocation())))
            .orElse(null);
    }

    protected void damage(Player source, LivingEntity target, double amount) {
        target.damage(amount, source);
    }

    protected double maxHealth(LivingEntity entity) {
        AttributeInstance attribute = entity.getAttribute(Attribute.MAX_HEALTH);
        return attribute != null ? attribute.getValue() : entity.getHealth();
    }

    protected boolean isStandingOnSolidGround(Player player) {
        if (player.isFlying() || player.isGliding()) {
            return false;
        }
        Location belowFeet = player.getLocation().subtract(0.0, 0.15, 0.0);
        return !belowFeet.getBlock().isPassable();
    }
}
