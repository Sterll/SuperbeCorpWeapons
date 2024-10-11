package fr.yanis.superbecorpweapons;

import fr.yanis.superbecorpweapons.item.ItemsEvents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
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
