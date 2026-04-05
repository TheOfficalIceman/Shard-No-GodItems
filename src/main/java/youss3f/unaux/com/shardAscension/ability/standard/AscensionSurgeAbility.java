package youss3f.unaux.com.shardascension.ability.standard;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class AscensionSurgeAbility extends AbstractAbility {
    public AscensionSurgeAbility() { super("ascension_surge", "Ascension Surge", NamedTextColor.LIGHT_PURPLE, "✺", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        int duration = integer(plugin, "duration-ticks", 120);
        AbilityEffects.buff(player, PotionEffectType.SPEED, duration, 1);
        AbilityEffects.buff(player, PotionEffectType.STRENGTH, duration, 1);
        AbilityEffects.buff(player, PotionEffectType.REGENERATION, duration, 0);
        List<LivingEntity> targets = nearbyTargets(player, number(plugin, "radius", 6));
        targets.forEach(target -> damage(player, target, number(plugin, "damage", 8)));
        AbilityEffects.pushOut(targets, player.getLocation(), 1.4, 0.35);
        particles(plugin, player.getLocation(), Particle.END_ROD, 60, 0.8, 1, 0.8, 0.04);
        sound(plugin, player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1f, 1.5f);
    }
}
