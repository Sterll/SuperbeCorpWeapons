package fr.yanis.superbecorpweapons;

import org.bukkit.plugin.java.JavaPlugin;

public final class SCWMain extends JavaPlugin {

    public static SCWMain instance;

    @Override
    public void onEnable() {
        getCommand("weapons").setExecutor(new CommandWeapons());
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
