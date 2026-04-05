package youss3f.unaux.com.shardascension.ability.combined;

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

public final class VampiricSurgeAbility extends AbstractAbility {
    public VampiricSurgeAbility() { super("vampiric_surge", "Vampiric Surge", NamedTextColor.DARK_RED, "☤", true); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        List<LivingEntity> targets = nearbyTargets(player, number(plugin, "radius", 7));
        double heal = 0;
        for (LivingEntity target : targets) {
            damage(player, target, number(plugin, "damage", 5));
            heal += number(plugin, "heal", 3.5);
            particles(plugin, target.getLocation().add(0, 1, 0), Particle.SCULK_SOUL, 14, 0.2, 0.4, 0.2, 0.02);
        }
        player.setHealth(Math.min(maxHealth(player), player.getHealth() + heal));
        AbilityEffects.dustBurst(plugin, player.getLocation().add(0, 1, 0), Color.fromRGB(170, 0, 0), 30);
        sound(plugin, player.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1.8f);
    }
}
