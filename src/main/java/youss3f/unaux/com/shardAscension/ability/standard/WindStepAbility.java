package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class WindStepAbility extends AbstractAbility {
    public WindStepAbility() { super("wind_step", "Wind Step", NamedTextColor.GREEN, "❈", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        int duration = integer(plugin, "duration-ticks", 80);
        AbilityEffects.buff(player, PotionEffectType.SPEED, duration, integer(plugin, "speed-amplifier", 2));
        AbilityEffects.buff(player, PotionEffectType.JUMP_BOOST, duration, integer(plugin, "jump-amplifier", 1));
        particles(plugin, player.getLocation(), Particle.CLOUD, 25, 0.4, 0.7, 0.4, 0.04);
        sound(plugin, player.getLocation(), Sound.ENTITY_BREEZE_IDLE_AIR, 1f, 1.4f);
    }
}
