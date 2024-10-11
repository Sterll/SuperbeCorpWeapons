package fr.yanis.superbecorpweapons.item.management;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemsEvents implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        ItemManager.getItems().forEach((item, itemManager) -> {
            if(event.getItem() == item) {
                itemManager.item().onUse(event);
            }
        });
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event){
        ItemManager.getItems().forEach((item, itemManager) -> {
            if(event.getPlayer().getInventory().getItemInMainHand() == item) {
                itemManager.item().onUseAtEntity(event);
            }
        });
    }
}