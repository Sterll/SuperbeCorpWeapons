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
        String url = "https://www.dropbox.com/scl/fi/m2fnuvgoapt0233myn0hg/SandBlocks.zip?rlkey=owiaodk7vrfu90pjpu6p7okul&st=cdk36vjw&dl=1";
        String hash = "024f41c08d0dea42c4c4bdcd4caf1adc92d7801f";
        e.getPlayer().setResourcePack(url, hash);
    }
}
