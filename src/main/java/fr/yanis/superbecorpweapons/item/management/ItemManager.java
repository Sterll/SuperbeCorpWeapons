package fr.yanis.superbecorpweapons.item.management;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public record ItemManager(@NotNull Item item) {

    private static HashMap<ItemStack, ItemManager> items = new HashMap<>();

    public static HashMap<ItemStack, ItemManager> getItems() {
        return items;
    }

    public ItemManager {
        items.put(item.getItem(), this);
    }
}
