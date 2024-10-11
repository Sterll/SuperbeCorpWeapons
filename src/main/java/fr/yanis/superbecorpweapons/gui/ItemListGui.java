package fr.yanis.superbecorpweapons.gui;

import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.ItemManager;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemListGui implements InventoryProvider {

    public final static RyseInventory inventory = RyseInventory.builder()
            .rows(3)
            .title("§6Liste des items")
            .provider(new ItemListGui())
            .build(SCWMain.getInstance());

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName(Component.text("§f")).build());

        for (ItemManager item : ItemManager.getItems().values()) {
            contents.add(IntelligentItem.of(item.item().getItem(), inventoryClickEvent -> {
                player.getInventory().addItem(item.item().getItem());
            }));
        }
    }
}
