package fr.yanis.superbecorpweapons.item.management;

import fr.yanis.superbecorpweapons.SCWMain;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemsEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (event.getItem() == null)
            return;

        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();

        assert ItemManager.key != null;
        if (!itemStack.getPersistentDataContainer().has(ItemManager.key, PersistentDataType.BYTE))
            return;

        byte id = itemStack.getPersistentDataContainer().get(ItemManager.key, PersistentDataType.BYTE);
        ItemManager value = ItemManager.getItems().get(id);
        Item item = value.item();

        if (item.getItem().isSimilar(event.getItem())) {
            item.cooldown.putIfAbsent(player.getUniqueId(), false);

            if (!item.cooldown.get(id))
                item.onUse(event, value);

            if (item.getCooldown() != 0) {
                item.cooldown.put(player.getUniqueId(), true);
                player.sendMessage(Component.text("§cVous ne pouvez pas réutiliser l'item : " + item.getName() + " pendant " + item.getCooldown() + " secondes"));
                @NotNull CoolDownTask coolDownTask = new CoolDownTask(value, event);
                coolDownTask.runTaskTimerAsynchronously(SCWMain.getInstance(), 0, 20);
                value.item().cooldownTask.put(player.getUniqueId(), coolDownTask);
            }

        } else {
            player.sendMessage(Component.text("§cVous ne pouvez pas réutiliser l'item : " + value.item().getName() + " pendant encore " + item.getTimeLeft(player.getUniqueId()) + " secondes"));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        for (@NotNull ItemManager value : ItemManager.getItems().values()) {
            value.item().onQuit(e);
        }
    }

    @EventHandler
    public void onProjectileHit(@NotNull ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        for (@NotNull ItemManager value : ItemManager.getItems().values()) {
            value.item().onProjectileHit(event);
        }
    }

    @EventHandler
    public void onEntityDamageByPlayer(@NotNull EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        for (@NotNull ItemManager value : ItemManager.getItems().values()) {
            value.item().onAttackEntity(event);
        }
    }

    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        for (@NotNull ItemManager value : ItemManager.getItems().values()) {
            value.item().onEntityDeath(event);
        }
    }

}
