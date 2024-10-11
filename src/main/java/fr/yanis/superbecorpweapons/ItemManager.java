package fr.yanis.superbecorpweapons;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public record ItemManager(Item item) {

    private static HashMap<ItemStack, ItemManager> items;

    public static HashMap<ItemStack, ItemManager> getItems() {
        return items;
    }
}
