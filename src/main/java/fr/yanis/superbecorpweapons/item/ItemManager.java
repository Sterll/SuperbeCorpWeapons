package fr.yanis.superbecorpweapons.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public record ItemManager(@NotNull Item item) {

    private static HashMap<ItemStack, ItemManager> items;

    public static HashMap<ItemStack, ItemManager> getItems() {
        return items;
    }
}
