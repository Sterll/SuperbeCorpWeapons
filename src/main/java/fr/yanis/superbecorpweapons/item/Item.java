package fr.yanis.superbecorpweapons.item;

import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Item {

    public abstract String getKey();
    public abstract String getName();
    public abstract String getDescription();
    public abstract ItemStack getItem();

    public abstract void onUse(PlayerInteractEvent e);
    public abstract void onUseAtEntity(PlayerInteractAtEntityEvent e);

}
