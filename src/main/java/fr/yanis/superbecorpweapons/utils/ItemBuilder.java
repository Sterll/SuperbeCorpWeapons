package fr.yanis.superbecorpweapons.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private final List<Component> lore;
    private final Map<Enchantment, Integer> enchantments;

    // Constructor
    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        this.lore = new ArrayList<>();
        this.enchantments = new HashMap<>();
    }

    // Set the display name with Component
    public ItemBuilder setName(Component name) {
        itemMeta.displayName(name);
        return this;
    }

    // Add lore lines using Component
    public ItemBuilder addLore(Component... lines) {
        this.lore.addAll(Arrays.asList(lines));
        this.itemMeta.lore(lore);
        return this;
    }

    // Set lore using a list of Component
    public ItemBuilder setLore(List<Component> lore) {
        this.lore.clear();
        this.lore.addAll(lore);
        itemMeta.lore(this.lore);
        return this;
    }

    // Add an enchantment with a specific level
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    // Remove a specific enchantment
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        enchantments.remove(enchantment);
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    // Hide item flags (e.g., enchantment glow)
    public ItemBuilder addItemFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    // Set item amount
    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    // Mark as unbreakable
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    // Apply custom model data
    public ItemBuilder setCustomModelData(Integer data) {
        itemMeta.setCustomModelData(data);
        return this;
    }

    // Set item glow without enchantment text
    public ItemBuilder setGlow(boolean glow) {
        if (glow) {
            addEnchantment(Enchantment.LUCK_OF_THE_SEA, 1);
            addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            removeEnchantment(Enchantment.LUCK_OF_THE_SEA);
        }
        return this;
    }

    // Finalize and return the item
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}