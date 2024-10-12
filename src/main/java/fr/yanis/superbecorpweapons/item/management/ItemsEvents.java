package fr.yanis.superbecorpweapons.item.management;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemsEvents implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        for (ItemManager value : ItemManager.getItems().values()) {
            if(value.item().getItem().isSimilar(event.getItem())) {
                value.item().onUse(event);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event){
        for (ItemManager value : ItemManager.getItems().values()) {
            if(value.item().getItem().isSimilar(event.getPlayer().getActiveItem())) {
                value.item().onUseAtEntity(event);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player)) return;
        for (ItemManager value : ItemManager.getItems().values()) {
            value.item().onProjectileHit(event);
        }
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        for (ItemManager value : ItemManager.getItems().values()) {
            if(value.item().getItem().isSimilar(((Player) event.getDamager()).getActiveItem())){
                value.item().onAttackEntity(event);
            }
        }
    }
}