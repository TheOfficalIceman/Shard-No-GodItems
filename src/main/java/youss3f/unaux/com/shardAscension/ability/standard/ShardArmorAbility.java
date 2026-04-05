package youss3f.unaux.com.shardascension.ability.standard;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;
import youss3f.unaux.com.shardascension.ability.AbstractAbility;
import youss3f.unaux.com.shardascension.util.AbilityEffects;

public final class ShardArmorAbility extends AbstractAbility {
    public ShardArmorAbility() { super("shard_armor", "Shard Armor", NamedTextColor.AQUA, "⛨", false); }
    @Override
    public void activate(ShardAscensionPlugin plugin, Player player) {
        player.setAbsorptionAmount(Math.min(
            player.getAttribute(Attribute.MAX_ABSORPTION).getValue(),
            player.getAbsorptionAmount() + number(plugin, "absorption", 8)
        ));
        AbilityEffects.buff(player, PotionEffectType.RESISTANCE, integer(plugin, "duration-ticks", 140), 0);
        particles(plugin, player.getLocation(), Particle.ITEM_SNOWBALL, 35, 0.5, 0.9, 0.5, 0.05);
        sound(plugin, player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 0.7f);
    }
}
