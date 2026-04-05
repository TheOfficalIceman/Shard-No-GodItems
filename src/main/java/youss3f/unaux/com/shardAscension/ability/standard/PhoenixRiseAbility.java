package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class PhoenixRiseAbility extends AbstractAbility {
    public PhoenixRiseAbility() { super("phoenix_rise", "Phoenix Rise", NamedTextColor.GOLD, "🕊", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        player.setHealth(Math.min(maxHealth(player), player.getHealth() + number(plugin, "heal", 8)));
        player.setVelocity(player.getVelocity().setY(1.15));
        AbilityEffects.buff(player, PotionEffectType.FIRE_RESISTANCE, integer(plugin, "duration-ticks", 120), 0);
        particles(plugin, player.getLocation(), Particle.FLAME, 45, 0.5, 0.8, 0.5, 0.08);
        particles(plugin, player.getLocation(), Particle.TOTEM_OF_UNDYING, 15, 0.4, 0.7, 0.4, 0.02);
        sound(plugin, player.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 1.1f);
    }
}
