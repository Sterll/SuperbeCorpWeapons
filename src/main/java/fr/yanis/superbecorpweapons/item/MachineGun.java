package fr.yanis.superbecorpweapons.item;

import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
        e.getPlayer().sendMessage("§bOUI ! ");
        int amountOfShoot = 5;
        double spread = 0.1;

        for (int i = 0; i < amountOfShoot; i++) {
            Arrow arrow = e.getPlayer().launchProjectile(Arrow.class);
            arrow.setVelocity(
                    arrow.getVelocity().add(new Vector(
                            Math.random() * spread - spread / 2,
                            Math.random() * spread - spread / 2,
                            Math.random() * spread - spread / 2
                    ))
            );
            arrow.setDamage(1.0);
            arrow.setCritical(false);

        }
    }
}
