package fr.yanis.superbecorpweapons.item;

import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.AItem;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import fr.yanis.superbecorpweapons.utils.ParticleLib;
import net.kyori.adventure.text.Component;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@AItem(defaultName = "§9Staff des invocateurs", defaultDescription = "§bC'est juste un baton qui invoque des mobs", defaultCooldown = 5)
public class SummonersStaff extends Item {

    private HashMap<UUID, ArrayList<Zombie>> zombies = new HashMap<>();

    public SummonersStaff() {
        super();
    }

    @Override
    public String getKey() {
        return "summoners_staff";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .setCustomModelData(26)
                .build();
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

        Zombie zombie = player.getWorld().spawn(spawnLocation, Zombie.class, z -> {
            z.customName(Component.text("§cZombie de §b" + e.getPlayer().getName()));
            z.setCustomNameVisible(true);
            z.setTarget(null);
            z.setAdult();
            z.setAI(true);
            z.getEquipment().setHelmet(new ItemStack(Material.ZOMBIE_HEAD));
            ParticleLib.spawnRandomParticles(z, Color.AQUA);
            addZombie(player.getUniqueId(), z);
            moveZombie(z, e.getPlayer());
        });
        e.getPlayer().playSound(e.getPlayer().getLocation(), "minecraft:custom.summoners_sound", 1.0f, 1.0f);
    }

    @Override
    public void onAttackEntity(@NotNull EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player)) return;
        UUID uuid = e.getDamager().getUniqueId();
        if(zombies.containsKey(uuid)) {
            for(Zombie zombie : zombies.get(uuid)) {
                zombie.setTarget((LivingEntity) e.getEntity());
                zombie.setAI(true);
            }
        }
    }

    @Override
    public void onEntityDeath(@NotNull EntityDeathEvent e) {
        if(e.getEntity() instanceof Zombie) {
            for(UUID uuid : zombies.keySet()) {
                if(zombies.get(uuid).contains((Zombie) e.getEntity())) {
                    removeZombie(uuid, (Zombie) e.getEntity());
                    return;
                }
            }
        }
        for (UUID uuid : zombies.keySet()) {
            for (Zombie zombie : zombies.get(uuid)) {
                if (zombie.getTarget() == e.getEntity()) {
                    zombie.setTarget(null);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (UUID uuid : zombies.keySet()) {
            for (Zombie zombie : zombies.get(uuid)) {
                zombie.remove();
                zombies.get(uuid).remove(zombie);
            }
        }
    }

    @Override
    public void onQuit(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        if(zombies.containsKey(uuid)){
            for(Zombie zombie : zombies.get(uuid)){
                zombie.remove();
            }
            zombies.remove(uuid);
        }
    }

    public void addZombie(UUID uuid, Zombie zombie) {
        if (!zombies.containsKey(uuid)) {
            zombies.put(uuid, new ArrayList<>());
        }
        zombies.get(uuid).add(zombie);
    }

    public void removeZombie(UUID uuid, Zombie zombie) {
        if (zombies.containsKey(uuid)) {
            zombies.get(uuid).remove(zombie);
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
