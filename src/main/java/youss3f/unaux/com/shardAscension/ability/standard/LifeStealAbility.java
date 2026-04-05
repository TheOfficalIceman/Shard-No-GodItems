package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class LifeStealAbility extends AbstractAbility {
    public LifeStealAbility() { super("life_steal", "Life Steal", NamedTextColor.DARK_RED, "🩸", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        LivingEntity target = nearestTarget(player, number(plugin, "range", 8));
        if (target == null) {
            return;
        }
        damage(player, target, number(plugin, "damage", 5));
        player.setHealth(Math.min(maxHealth(player), player.getHealth() + number(plugin, "heal", 4)));
        AbilityEffects.dustBurst(plugin, target.getLocation().add(0, 1, 0), Color.RED, 24);
        particles(plugin, target.getLocation(), Particle.SCULK_SOUL, 20, 0.3, 0.5, 0.3, 0.01);
        sound(plugin, player.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1.5f);
    }
}
