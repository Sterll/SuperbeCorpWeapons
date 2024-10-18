package fr.yanis.superbecorpweapons.item;

import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.AItem;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@AItem(defaultName = "§bTrident de Poséidon", defaultDescription = "§bUn trident qui vous permet de lancer une vague", defaultCooldown = 10)
public class PoseidonsTrident extends Item {

    private ArrayList<Entity> alreadyHit = new ArrayList<>();

    public PoseidonsTrident(){
        super();
    }

    @Override
    public String getKey() {
        return "poseidon_trident";
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .setCustomModelData(25)
                .addPersistantData(ItemManager.key, PersistentDataType.BYTE, getID())
                .build();
    }

    @Override
    public byte getID() {
        return 2;
    }

    @Override
    public void onUse(@NotNull PlayerInteractEvent e, @NotNull ItemManager itemManager) {
        Player player = e.getPlayer();

        ParticleLib.spawnWaterWaves(player, itemManager);
        player.playSound(player.getLocation(), "minecraft:custom.trident_sound", 1.0f, 1.0f);
    }

    @Override
    public void whenEntityIsTouchedByParticle(@NotNull Entity entity, @NotNull ItemManager itemManager) {
        if(!(entity instanceof LivingEntity))
            return;
        if(alreadyHit.contains(entity))
            return;

        ParticleLib.spawnRotatingCircle(entity, Color.BLUE);

        alreadyHit.add(entity);
        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getScheduler().runTask(SCWMain.getInstance(), () -> entity.setVelocity(entity.getVelocity().setY(2)));
                alreadyHit.remove(entity);
            }
        }.runTaskLaterAsynchronously(SCWMain.getInstance(), 20 * 3);
    }
}
