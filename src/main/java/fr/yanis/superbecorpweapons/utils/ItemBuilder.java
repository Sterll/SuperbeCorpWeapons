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


    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        this.lore = new ArrayList<>();
        this.enchantments = new HashMap<>();
    }

    public ItemBuilder setName(Component name) {
        itemMeta.displayName(name);
        return this;
    }

    public ItemBuilder addLore(Component... lines) {
        this.lore.addAll(Arrays.asList(lines));
        this.itemMeta.lore(lore);
        return this;
    }

    public ItemBuilder setLore(List<Component> lore) {
        this.lore.clear();
        this.lore.addAll(lore);
        itemMeta.lore(this.lore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        enchantments.remove(enchantment);
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setCustomModelData(Integer data) {
        itemMeta.setCustomModelData(data);
        return this;
    }

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