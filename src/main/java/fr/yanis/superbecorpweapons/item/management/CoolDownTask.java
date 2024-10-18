package fr.yanis.superbecorpweapons.item.management;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class CoolDownTask extends BukkitRunnable {

    private final byte id;
    private final UUID uuid;
    private int time;

    public CoolDownTask(ItemManager value, @NotNull PlayerInteractEvent e) {
        this.id = value.item().getID();
        this.uuid = e.getPlayer().getUniqueId();
        this.time = value.item().getCooldown();
    }

    @Override
    public void run() {

        if(time == 0){
            ItemManager value = Objects.requireNonNull(ItemManager.getItem(id), "Aucun item trouvé avec cet ID");

            value.item().cooldown.put(uuid, false);
            Objects.requireNonNull(Bukkit.getPlayer(uuid), "Aucun joueur trouvé avec cet UUID").sendMessage(Component.text("§aVous pouvez réutiliser l'item : " + value.item().getName()));

            this.cancel();
        }

        this.time--;

    }

    public int getTime() {
        return this.time;
    }

}
