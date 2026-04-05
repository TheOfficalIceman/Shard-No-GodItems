package youss3f.unaux.com.shardascension.ability.standard;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class TimeBurstAbility extends AbstractAbility {
    public TimeBurstAbility() { super("time_burst", "Time Burst", NamedTextColor.GRAY, "⌛", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        int duration = integer(plugin, "duration-ticks", 80);
        List<LivingEntity> targets = nearbyTargets(player, number(plugin, "radius", 6));
        targets.forEach(target -> target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, 3, true, true, true)));
        AbilityEffects.buff(player, PotionEffectType.SPEED, duration, 1);
        particles(plugin, player.getLocation(), Particle.ENCHANT, 45, 0.7, 0.9, 0.7, 0.2);
        sound(plugin, player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.9f, 1.6f);
    }
}
