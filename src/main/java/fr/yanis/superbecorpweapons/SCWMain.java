package fr.yanis.superbecorpweapons;

import fr.yanis.superbecorpweapons.item.ItemsEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class SCWMain extends JavaPlugin {

    public static SCWMain instance;

    @Override
    public void onEnable() {
        getCommand("weapons").setExecutor(new CommandWeapons());
        getServer().getPluginManager().registerEvents(new ItemsEvents(), this);
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SCWMain getInstance() {
        return instance;
    }
}
