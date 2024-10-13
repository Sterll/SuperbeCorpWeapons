package fr.yanis.superbecorpweapons.item;

import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import fr.yanis.superbecorpweapons.utils.ParticleLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class SummonersStaff extends Item {

    private HashMap<Player, ArrayList<Zombie>> zombies = new HashMap<>();

    @Override
    public String getKey() {
        return "summoners_staff";
    }

    @Override
    public String getName() {
        return "§9Staff des invocateurs";
    }

    @Override
    public String getDescription() {
        return "§bC'est juste un baton qui invoque des mobs";
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
    public void onUse(PlayerInteractEvent e) {
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

        Zombie zombie = player.getWorld().spawn(spawnLocation, Zombie.class);
        zombie.customName(Component.text("§cZombie de §b" + e.getPlayer().getName()));
        zombie.setCustomNameVisible(true);
        zombie.setTarget(null);
        zombie.setAdult();
        zombie.setAI(true);
        zombie.getEquipment().setHelmet(new ItemStack(Material.ZOMBIE_HEAD));
        ParticleLib.spawnRandomParticles(zombie, Color.AQUA);
        addZombie(e.getPlayer(), zombie);
        moveZombie(zombie, e.getPlayer());
    }

    @Override
    public void onAttackEntity(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player)) return;
        if(zombies.containsKey((Player) e.getDamager())) {
            for(Zombie zombie : zombies.get((Player) e.getDamager())) {
                zombie.setTarget((LivingEntity) e.getEntity());
                zombie.setAI(true);
            }
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        if(e.getEntity() instanceof Zombie) {
            for(Player player : zombies.keySet()) {
                if(zombies.get(player).contains((Zombie) e.getEntity())) {
                    removeZombie(player, (Zombie) e.getEntity());
                    return;
                }
            }
        }
        for (Player player : zombies.keySet()) {
            for (Zombie zombie : zombies.get(player)) {
                if (zombie.getTarget() == e.getEntity()) {
                    zombie.setTarget(null);
                }
            }
        }
    }

    public void addZombie(Player player, Zombie zombie) {
        if (!zombies.containsKey(player)) {
            zombies.put(player, new ArrayList<>());
        }
        zombies.get(player).add(zombie);
    }

    public void removeZombie(Player player, Zombie zombie) {
        if (zombies.containsKey(player)) {
            zombies.get(player).remove(zombie);
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
