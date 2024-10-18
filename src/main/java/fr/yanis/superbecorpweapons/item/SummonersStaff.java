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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@AItem(defaultName = "§9Staff des invocateurs", defaultDescription = "§bC'est juste un baton qui invoque des mobs", defaultCooldown = 5)
public class SummonersStaff extends Item {

    // HashMap<UUID, Set<UUID>> : UUID du joueur, Set de UUID des zombies invoqués
    private HashMap<UUID, Set<UUID>> zombies = new HashMap<>();

    public SummonersStaff() {
        super();
    }

    @Override
    public String getKey() {
        return "summoners_staff";
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .setCustomModelData(26)
                .addPersistantData(ItemManager.key, PersistentDataType.BYTE, getID())
                .build();
    }

    @Override
    public byte getID() {
        return 0;
    }

    @Override
    public void onUse(@NotNull PlayerInteractEvent e, @NotNull ItemManager itemManager) {
        Player player = e.getPlayer();
        Location[] possibleLocations = {
                player.getLocation().add(4, 0, 0),  // Devant
                player.getLocation().add(-4, 0, 0), // Derrière
                player.getLocation().add(0, 0, 4),  // Droite
                player.getLocation().add(0, 0, -4)  // Gauche
        };

        Location spawnLocation = null;
        for (Location loc : possibleLocations) {
            Block block = loc.getBlock();
            Block blockAbove = loc.add(0, 1, 0).getBlock();
            if (block.isPassable() && blockAbove.isPassable()) {
                spawnLocation = loc;
                break;
            }
        }

        if (spawnLocation == null) {
            player.sendMessage("§cAucun espace libre trouvé pour faire apparaître le zombie !");
            return;
        }

        player.getWorld().spawn(spawnLocation, Zombie.class, z -> {
            z.customName(Component.text("§cZombie de §b" + e.getPlayer().getName()));
            z.setCustomNameVisible(true);
            z.setTarget(null);
            z.setAdult();
            z.setAI(true);
            z.getEquipment().setHelmet(new ItemStack(Material.ZOMBIE_HEAD));
            ParticleLib.spawnRandomParticles(z, Color.AQUA);
            addZombie(player.getUniqueId(), z.getUniqueId());
            moveZombie(z, e.getPlayer());
        });
        e.getPlayer().playSound(e.getPlayer().getLocation(), "minecraft:custom.summoners_sound", 1.0f, 1.0f);
    }

    @Override
    public void onAttackEntity(@NotNull EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player))
            return;
        if (!(e.getEntity() instanceof LivingEntity entity))
            return;

        UUID uuid = player.getUniqueId();

        if( zombies.containsKey(uuid)) {
            if (zombies.get(uuid).isEmpty())
                return;

            for(UUID zombieUUID : zombies.get(uuid)) {
                if(Bukkit.getEntity(zombieUUID) instanceof Zombie zombie){
                    zombie.setTarget(entity);
                    zombie.setAI(true);
                }
            }
        }

    }

    @Override
    public void onEntityDeath(@NotNull EntityDeathEvent e) {

        if (e.getEntity() instanceof Zombie zombie) {
            UUID zombieUUID = zombie.getUniqueId();
            for(UUID uuid : zombies.keySet()) {
                if(zombies.get(uuid).contains(zombieUUID)) {
                    removeZombie(uuid, zombieUUID);
                    return;
                }
            }
        }

        for (UUID uuid : zombies.keySet()) {
            for (UUID zombieUUID : zombies.get(uuid)) {

                if (Bukkit.getEntity(zombieUUID) instanceof Zombie zombie){
                    if (zombie.getTarget() == e.getEntity()) {
                        zombie.setTarget(null);
                    }
                }
            }
        }

    }

    @Override
    public void onDisable() {

        for (UUID uuid : zombies.keySet()) {
            for (UUID zombieUUID : zombies.get(uuid)) {

                if (!(Bukkit.getEntity(zombieUUID) instanceof Zombie zombie))
                    return;

                zombie.remove();
                zombies.get(uuid).remove(zombieUUID);
            }
        }

    }

    @Override
    public void onQuit(PlayerQuitEvent e){
        super.onQuit(e);
        UUID uuid = e.getPlayer().getUniqueId();

        if(zombies.containsKey(uuid)){
            for (UUID zombieUUID : zombies.get(uuid)){

                if (!(Bukkit.getEntity(zombieUUID) instanceof Zombie zombie))
                    return;

                zombie.remove();
            }
            zombies.remove(uuid);
        }

    }

    public void addZombie(UUID uuid, UUID zombieUUID) {
        if (!zombies.containsKey(uuid)) {
            zombies.put(uuid, Set.of());
        }

        zombies.get(uuid).add(zombieUUID);
    }

    public void removeZombie(UUID uuid, UUID zombieUUID) {
        if (zombies.containsKey(uuid)) {
            zombies.get(uuid).remove(zombieUUID);
        }
    }

    public void moveZombie(Zombie zombie, Player player){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!zombie.isValid() || !player.isOnline()) {
                    this.cancel();
                    return;
                }
                if(zombie.getTarget() != player && zombie.getTarget() != null) return;

                Location playerLocation = player.getLocation();
                double distance = zombie.getLocation().distance(playerLocation);

                if (distance > 4) {
                    zombie.setAI(true);
                    zombie.setTarget(player);
                } else {
                    zombie.setAI(false);
                    zombie.setTarget(null);
                }
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 10L);
    }
}
