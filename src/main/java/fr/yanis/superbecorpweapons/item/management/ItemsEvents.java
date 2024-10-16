package fr.yanis.superbecorpweapons.item.management;

import fr.yanis.superbecorpweapons.SCWMain;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ItemsEvents implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        for (ItemManager value : ItemManager.getItems().values()) {
            if(value.item().getItem().isSimilar(event.getItem())) {
                value.item().cooldown.putIfAbsent(event.getPlayer(), false);
                if(!(value.item().cooldown.get(event.getPlayer()))){
                    value.item().onUse(event);
                    if(value.item().getCooldown() != 0){
                        value.item().cooldown.put(event.getPlayer(), true);
                        event.getPlayer().sendMessage(Component.text("§cVous ne pouvez pas réutiliser l'item : " + value.item().getName() + " pendant " + value.item().getCooldown() + "secondes"));
                        CoolDownTask coolDownTask = new CoolDownTask(value, event);
                        coolDownTask.runTaskTimerAsynchronously(SCWMain.getInstance(), 0, 20);
                        value.item().cooldownTask.put(event.getPlayer(), coolDownTask);
                    }
                } else {
                    event.getPlayer().sendMessage(Component.text("§cVous ne pouvez pas réutiliser l'item : " + value.item().getName() + " pendant encore " + value.item().getTimeLeft(event.getPlayer()) + "secondes"));
                }
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
            value.item().onAttackEntity(event);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        for (ItemManager value : ItemManager.getItems().values()) {
            value.item().onEntityDeath(event);
        }
    }
}