package fr.yanis.superbecorpweapons.item;

import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import fr.yanis.superbecorpweapons.utils.ParticleLib;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class PoseidonsTrident extends Item {

    private ArrayList<Entity> alreadyHit = new ArrayList<>();

    @Override
    public String getKey() {
        return "poseidon_trident";
    }

    @Override
    public String getName() {
        return "§bTrident de Poséidon";
    }

    @Override
    public String getDescription() {
        return "§bUn trident qui vous permet de lancer une vague";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .setCustomModelData(25)
                .build();
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public void onUse(PlayerInteractEvent e) {
        ParticleLib.spawnWaterWaves(e.getPlayer(), this);
        e.getPlayer().playSound(e.getPlayer().getLocation(), "minecraft:custom.trident_sound", 1.0f, 1.0f);
    }

    @Override
    public void whenEntityIsTouchedByParticle(Entity entity) {
        if(!(entity instanceof LivingEntity)) return;
        if(alreadyHit.contains(entity)) return;
        ParticleLib.spawnRotatingCircle(entity, Color.BLUE);
        alreadyHit.add(entity);
        new BukkitRunnable(){
            @Override
            public void run() {
                entity.setVelocity(entity.getVelocity().setY(2));
                alreadyHit.remove(entity);
            }
        }.runTaskLaterAsynchronously(SCWMain.getInstance(), 20 * 3);
    }
}
