package fr.yanis.superbecorpweapons;

import fr.yanis.superbecorpweapons.command.CommandWeapons;
import fr.yanis.superbecorpweapons.item.*;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import fr.yanis.superbecorpweapons.item.management.ItemsEvents;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SCWMain extends JavaPlugin implements Listener {

    public static SCWMain instance;

    public InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        getCommand("weapons").setExecutor(new CommandWeapons());
        getServer().getPluginManager().registerEvents(new ItemsEvents(), this);
        getServer().getPluginManager().registerEvents(this, this);

        inventoryManager = new InventoryManager(this);
        inventoryManager.invoke();

        new ItemManager(new MachineGun());
        new ItemManager(new ExplosiveGun());
        new ItemManager(new PoseidonsTrident());
        new ItemManager(new SummonersStaff());
        new ItemManager(new LaserGun());

        instance = this;
    }

    @Override
    public void onDisable() {
        ItemManager.getItems().values().forEach(itemManager -> itemManager.item().onDisable());
    }

    public static SCWMain getInstance() {
        return instance;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        String url = "https://www.dropbox.com/scl/fi/4l2x2ggiu6d6cldq8k6tt/SandBlocks.zip?rlkey=b30gapdtre3mgjkp1t77vxfk6&st=zv5j7644&dl=1";
        String hash = "fc07409081a6b1d98afab1be56a0664ae93c3baa";
        e.getPlayer().setResourcePack(url, hash);
    }
}
