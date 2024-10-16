package fr.yanis.superbecorpweapons.item.management;

import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.config.BooleanConfig;
import fr.yanis.superbecorpweapons.item.management.config.IntConfig;
import fr.yanis.superbecorpweapons.item.management.config.StringConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;

public record ItemManager(@NotNull Item item) {

    private static HashMap<ItemStack, ItemManager> items = new HashMap<>();

    @NotNull
    public static HashMap<ItemStack, ItemManager> getItems() {
        return items;
    }

    public static File file = new File(SCWMain.getInstance().getDataFolder(), "items.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    @NotNull
    public static File getFile() { return file; }
    @NotNull
    public static FileConfiguration getConfig() { return config; }
    @Nullable
    public static ConfigurationSection getSection(String key) { return config.getConfigurationSection(key); }

    @Nullable
    public ConfigurationSection getSection() { return getSection(item.getKey()); }

    public static ItemManager getItem(@NotNull ItemStack it) {
        if(items.containsKey(it)) {
            return items.get(it);
        }
        return null;
    }

    public ItemManager {
        items.put(item.getItem(), this);
    }

    public void init(){
        try {
            config.set(item.getKey() + ".name", item.getAnnotation().defaultName());
            config.set(item.getKey() + ".description", item.getAnnotation().defaultDescription());
            config.set(item.getKey() + ".cooldown", item.getAnnotation().defaultCooldown());

            for (StringConfig stringConfig : item().getAnnotation().stringConfigValues()) {
                config.set(item.getKey() + "." + stringConfig.name(), stringConfig.value());
            }
            for (IntConfig intConfig : item().getAnnotation().intConfigValues()) {
                config.set(item.getKey() + "." + intConfig.name(), intConfig.value());
            }
            for (BooleanConfig booleanConfig : item().getAnnotation().booleanConfigValues()) {
                config.set(item.getKey() + "." + booleanConfig.name(), booleanConfig.value());
            }
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
