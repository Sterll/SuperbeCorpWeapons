package fr.yanis.superbecorpweapons;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e){
        String url = SCWMain.getInstance().getConfig().getString("resource-pack.url");
        String hash = SCWMain.getInstance().getConfig().getString("resource-pack.hash");

        if (url == null || hash == null)
            return;

        e.getPlayer().setResourcePack(url, hash);
    }

}
