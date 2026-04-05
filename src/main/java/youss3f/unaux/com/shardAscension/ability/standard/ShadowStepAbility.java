package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;

public final class ShadowStepAbility extends AbstractAbility {
    public ShadowStepAbility() { super("shadow_step", "Shadow Step", NamedTextColor.DARK_GRAY, "☠", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        LivingEntity target = nearestTarget(player, number(plugin, "range", 10));
        if (target == null) {
            return;
        }
        Location behind = target.getLocation().clone().subtract(target.getLocation().getDirection().normalize());
        particles(plugin, player.getLocation(), Particle.SMOKE, 25, 0.4, 0.8, 0.4, 0.05);
        player.teleport(behind);
        damage(player, target, number(plugin, "damage", 5));
        particles(plugin, target.getLocation(), Particle.SCULK_SOUL, 20, 0.3, 0.7, 0.3, 0.02);
        sound(plugin, player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.9f, 0.6f);
    }
}
