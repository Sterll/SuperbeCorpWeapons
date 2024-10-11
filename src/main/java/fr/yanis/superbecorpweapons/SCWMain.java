package fr.yanis.superbecorpweapons;

import fr.yanis.superbecorpweapons.command.CommandWeapons;
import fr.yanis.superbecorpweapons.item.*;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import fr.yanis.superbecorpweapons.item.management.ItemsEvents;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SCWMain extends JavaPlugin {

    public static SCWMain instance;

    public InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        getCommand("weapons").setExecutor(new CommandWeapons());
        getServer().getPluginManager().registerEvents(new ItemsEvents(), this);

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
        // Plugin shutdown logic
    }

    public static SCWMain getInstance() {
        return instance;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
