package youss3f.unaux.com.shardascension.item;

import java.util.List;
import java.util.Locale;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;
import youss3f.unaux.com.shardascension.ShardAscensionPlugin;

public final class ItemManager {

    private final ShardAscensionPlugin plugin;

    public ItemManager(ShardAscensionPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createFragment(int amount) {
        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD, amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(text("Ascension Fragment", NamedTextColor.AQUA));
        meta.lore(List.of(
            text("A crystallized remnant of power.", NamedTextColor.GRAY),
            text("Used in advanced ascension crafting.", NamedTextColor.DARK_AQUA)
        ));
        meta.getPersistentDataContainer().set(plugin.getFragmentKey(), PersistentDataType.BYTE, (byte) 1);
        setCustomModelData(meta, plugin.getConfig().getInt("items.fragment.custom-model-data", 10001));
        applyConfiguredEnchants(meta, "items.fragment");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createCombinationItem() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(text("Ability Combination Core", NamedTextColor.LIGHT_PURPLE));
        meta.lore(List.of(
            text("Right-click to fuse two compatible abilities.", NamedTextColor.GRAY),
            text("Consumes the core on successful fusion.", NamedTextColor.DARK_PURPLE)
        ));
        meta.getPersistentDataContainer().set(plugin.getCombinationKey(), PersistentDataType.BYTE, (byte) 1);
        setCustomModelData(meta, plugin.getConfig().getInt("items.combination.custom-model-data", 10002));
        applyConfiguredEnchants(meta, "items.combination");
        item.setItemMeta(meta);
        return item;
    }

    public boolean isFragment(ItemStack stack) {
        return hasMarker(stack, plugin.getFragmentKey());
    }

    public boolean isCombinationItem(ItemStack stack) {
        return hasMarker(stack, plugin.getCombinationKey());
    }

    public void registerRecipes() {
        Bukkit.removeRecipe(new NamespacedKey(plugin, "ability_combination_core"));
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "ability_combination_core"), createCombinationItem());
        recipe.shape("FFF", "FDF", "FSF");
        recipe.setIngredient('F', new RecipeChoice.ExactChoice(createFragment(1)));
        recipe.setIngredient('D', Material.DRAGON_EGG);
        recipe.setIngredient('S', Material.STICK);
        Bukkit.addRecipe(recipe);
    }

    private boolean hasMarker(ItemStack stack, NamespacedKey key) {
        if (stack == null || !stack.hasItemMeta()) {
            return false;
        }
        Byte marker = stack.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.BYTE);
        return marker != null && marker == (byte) 1;
    }

    private void applyConfiguredEnchants(ItemMeta meta, String path) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(path + ".enchants");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            if (key.equalsIgnoreCase("knockback") || key.equalsIgnoreCase("wind_burst")) {
                continue;
            }
            Enchantment enchantment = resolveEnchantment(key);
            if (enchantment != null) {
                meta.addEnchant(enchantment, Math.max(1, section.getInt(key, 1)), true);
            }
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    private void setCustomModelData(ItemMeta meta, int customModelData) {
        CustomModelDataComponent component = meta.getCustomModelDataComponent();
        component.setFloats(List.of((float) customModelData));
        meta.setCustomModelDataComponent(component);
    }

    private Enchantment resolveEnchantment(String key) {
        String normalized = key.toLowerCase(Locale.ROOT);
        NamespacedKey namespacedKey = normalized.contains(":")
            ? NamespacedKey.fromString(normalized)
            : NamespacedKey.minecraft(normalized);
        if (namespacedKey == null) {
            return null;
        }
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(namespacedKey);
    }

    private Component text(String value, NamedTextColor color) {
        return Component.text(value, color).decoration(TextDecoration.ITALIC, false);
    }
}
