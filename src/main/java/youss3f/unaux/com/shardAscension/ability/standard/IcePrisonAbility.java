package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class IcePrisonAbility extends AbstractAbility {
    public IcePrisonAbility() { super("ice_prison", "Ice Prison", NamedTextColor.AQUA, "❄", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        LivingEntity target = nearestTarget(player, number(plugin, "range", 10));
        if (target == null) {
            return;
        }
        int duration = integer(plugin, "duration-ticks", 80);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 10, true, true, true));
        target.setFreezeTicks(duration);
        Location location = target.getLocation();
        particles(plugin, location.add(0, 1, 0), Particle.SNOWFLAKE, 35, 0.5, 1, 0.5, 0.03);
        AbilityEffects.ring(plugin, location, Particle.FALLING_WATER, 1.6, 16);
        sound(plugin, location, Sound.BLOCK_GLASS_BREAK, 0.9f, 1.7f);
    }
}
