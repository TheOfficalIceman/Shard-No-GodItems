package youss3f.unaux.com.shardascension.ability.standard;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class VoidPullAbility extends AbstractAbility {
    public VoidPullAbility() { super("void_pull", "Void Pull", NamedTextColor.DARK_PURPLE, "☾", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        double radius = number(plugin, "radius", 7);
        List<LivingEntity> targets = nearbyTargets(player, radius);
        AbilityEffects.pullIn(targets, player.getLocation(), number(plugin, "force", 1.2));
        targets.forEach(target -> damage(player, target, number(plugin, "damage", 3)));
        particles(plugin, player.getLocation(), Particle.DRAGON_BREATH, 35, 0.6, 0.6, 0.6, 0.02);
        sound(plugin, player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 0.7f);
    }
}
