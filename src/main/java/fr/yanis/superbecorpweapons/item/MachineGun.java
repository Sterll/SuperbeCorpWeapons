package fr.yanis.superbecorpweapons.item;

import com.destroystokyo.paper.ParticleBuilder;
import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
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
                .setCustomModelData(27)
                .build();
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void onUse(PlayerInteractEvent e) {
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
            arrow.setVisibleByDefault(false);
            arrow.setDamage(1.0);
            arrow.setCritical(false);
            arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            arrow.customName(Component.text("machine_gun"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (arrow.isDead() || !arrow.isValid()) {
                        this.cancel();
                        return;
                    }
                    new ParticleBuilder(Particle.DUST)
                            .data(new Particle.DustOptions(Color.fromRGB(178, 70, 70), 1))
                            .count(2)
                            .offset(0.1, 0.1, 0.1)
                            .extra(0.01)
                            .location(arrow.getLocation())
                            .spawn().particle();
                }
            }.runTaskTimerAsynchronously(SCWMain.getInstance(), 0L, 0L);
            e.getPlayer().playSound(e.getPlayer().getLocation(), "minecraft:custom.machine_gun_sound", 1.0f, 1.0f);
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent e) {
        if(!(e.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) e.getEntity();
        if(arrow.customName().equals(Component.text("machine_gun"))){
            arrow.remove();
        }
    }
}
