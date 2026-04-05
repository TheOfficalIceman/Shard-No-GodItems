package youss3f.unaux.com.shardascension.ability.standard;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class ShardBurstAbility extends AbstractAbility {
    public ShardBurstAbility() { super("shard_burst", "Shard Burst", NamedTextColor.AQUA, "❖", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        double radius = number(plugin, "radius", 4);
        double damage = number(plugin, "damage", 6);
        List<LivingEntity> targets = nearbyTargets(player, radius);
        targets.forEach(target -> damage(player, target, damage));
        particles(plugin, player.getLocation(), Particle.ITEM_COBWEB, 45, 0.5, 0.8, 0.5, 0.05);
        AbilityEffects.dustBurst(plugin, player.getLocation().add(0, 1, 0), Color.AQUA, 30);
        sound(plugin, player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1f, 0.9f);
    }
}
