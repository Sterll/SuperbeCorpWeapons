package fr.yanis.superbecorpweapons.item.management;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class CoolDownTask extends BukkitRunnable {

    public ItemManager value;
    public PlayerInteractEvent e;
    public int time;

    public CoolDownTask(@NotNull ItemManager value, @NotNull PlayerInteractEvent e) {
        this.value = value;
        this.e = e;
        time = value.item().getCooldown();
    }

    @Override
    public void run() {
        if(time == 0){
            value.item().cooldown.put(e.getPlayer(), false);
            e.getPlayer().sendMessage(Component.text("§aVous pouvez réutiliser l'item : " + value.item().getName()));
            cancel();
        }
        time--;
    }

    public int getTime() {
        return time;
    }
}
