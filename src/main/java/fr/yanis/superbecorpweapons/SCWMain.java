package fr.yanis.superbecorpweapons;

import fr.yanis.superbecorpweapons.command.CommandWeapons;
import fr.yanis.superbecorpweapons.item.*;
import fr.yanis.superbecorpweapons.item.management.AItem;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import fr.yanis.superbecorpweapons.item.management.ItemsEvents;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import javax.annotation.concurrent.Immutable;
import java.util.Set;

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

        instance = this;

        loadAllItems();
    }

    @Override
    public void onDisable() {
        ItemManager.getItems().values().forEach(itemManager -> itemManager.item().onDisable());
    }

    @NotNull
    public static SCWMain getInstance() {
        return instance;
    }

    @NotNull
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e){
        String url = "https://www.dropbox.com/scl/fi/4l2x2ggiu6d6cldq8k6tt/SandBlocks.zip?rlkey=b30gapdtre3mgjkp1t77vxfk6&st=zv5j7644&dl=1";
        String hash = "fc07409081a6b1d98afab1be56a0664ae93c3baa";
        e.getPlayer().setResourcePack(url, hash);
    }

    public void loadAllItems() {
        Reflections reflections = new Reflections("fr.yanis.superbecorpweapons.item");
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(AItem.class);
        for (Class<?> clazz : annotatedClasses) {
            try {
                if (Item.class.isAssignableFrom(clazz)) {
                    Class<?> specificScenarioClass = clazz;
                    Item itemInstance = (Item) specificScenarioClass.getDeclaredConstructor().newInstance();
                    ItemManager im = new ItemManager(itemInstance);
                    if(im.getSection() == null) im.init();
                }
            } catch (Exception e) {
                getLogger().severe("Erreur lors du chargement de l'item " + clazz.getName());
                e.printStackTrace();
            }
        }
    }
}
