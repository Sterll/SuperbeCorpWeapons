package fr.yanis.superbecorpweapons;

import fr.yanis.superbecorpweapons.command.CommandWeapons;
import fr.yanis.superbecorpweapons.item.management.AItem;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import fr.yanis.superbecorpweapons.item.management.ItemsEvent;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.Objects;
import java.util.Set;

public final class SCWMain extends JavaPlugin implements Listener {

    public static SCWMain instance;

    public InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("weapons"), "La commande weapons n'a pas été trouvé").setExecutor(new CommandWeapons());
        this.getServer().getPluginManager().registerEvents(new ItemsEvent(), this);
        this.getServer().getPluginManager().registerEvents(this, this);

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.invoke();

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
        return this.inventoryManager;
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e){
        String url = getConfig().getString("resource-pack.url");
        String hash = getConfig().getString("resource-pack.hash");

        if (url == null || hash == null)
            return;

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
