package fr.yanis.superbecorpweapons.gui;

import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemListGui implements InventoryProvider {

    public final static RyseInventory inventory = RyseInventory.builder()
            .rows(6)
            .title("§6Liste des items")
            .provider(new ItemListGui())
            .build(SCWMain.getInstance());

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName(Component.text("§f")).build());

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(28);

        contents.set(5, 2, IntelligentItem.of(new ItemBuilder(Material.ARROW).
                setAmount(pagination.isFirst()
                        ? 1
                        : pagination.page() - 1)
                .setName(pagination.isFirst()
                        ? Component.text("§c§oC'est la première page")
                        : Component.text("§ePage §8⇒ §9" + pagination.newInstance(pagination).previous().page())).build(), event -> {
            if (pagination.isFirst()) {
                player.sendMessage("§c§oVous êtes déjà sur la première page.");
                return;
            }

            RyseInventory currentInventory = pagination.inventory();
            currentInventory.open(player, pagination.previous().page());
        }));

        pagination.iterator(SlotIterator.builder()
                .startPosition(10)
                .blackList(17,18,26,27,35,36)
                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                .build());

        for (ItemManager item : ItemManager.getItems().values()) {
            pagination.addItem(IntelligentItem.of(item.item().getItem(), inventoryClickEvent -> {
                player.getInventory().addItem(item.item().getItem());
            }));
        }

        int page = pagination.newInstance(pagination).next().page();

        contents.set(5, 6, IntelligentItem.of(new ItemBuilder(Material.ARROW)
                .setAmount((pagination.isLast() ? 1 : page))
                .setName(!pagination.isLast()
                        ? Component.text("§ePage §8⇒ §9" + page) :
                        Component.text("§c§oC'est la dernière page")).build(), event -> {
            if (pagination.isLast()) {
                player.sendMessage("§c§oVous êtes déjà sur la dernière page.");
                return;
            }

            RyseInventory currentInventory = pagination.inventory();
            currentInventory.open(player, pagination.next().page());
        }));
    }
}
