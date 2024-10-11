package fr.yanis.superbecorpweapons.item;

import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MachineGun extends Item {
    @Override
    public String getKey() {
        return "machine_gun";
    }

    @Override
    public String getName() {
        return "§6Mitraillette";
    }

    @Override
    public String getDescription() {
        return "§bC'est juste une mitraillette";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .build();
    }

    @Override
    public void onUse(PlayerInteractEvent e) {

    }

    @Override
    public void onUseAtEntity(PlayerInteractAtEntityEvent e) {

    }
}
