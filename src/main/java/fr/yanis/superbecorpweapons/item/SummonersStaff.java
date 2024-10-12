package fr.yanis.superbecorpweapons.item;

import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        addZombie(e.getPlayer(), zombie);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!zombie.isValid() || !e.getPlayer().isOnline()) {
                    this.cancel();
                    return;
                }

                Location playerLocation = player.getLocation();
                double distance = zombie.getLocation().distance(playerLocation);

                if (distance > 4) {
                    zombie.setAI(true);
                    zombie.setTarget(e.getPlayer());
                } else {
                    zombie.setAI(false);
                    zombie.setTarget(null);
                }
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 10L);
    }

    @Override
    public void onAttackEntity(EntityDamageByEntityEvent e) {
        if(zombies.containsKey(e.getDamager())) {
            for(Zombie zombie : zombies.get(e.getDamager())) {
                zombie.setTarget((LivingEntity) e.getEntity());
            }
        }
    }

    @Override
    public void onCombust(EntityCombustEvent e) {
        if(e.getEntity() instanceof Zombie) {
            if(e.getEntity().customName() != null) return;
            String name = e.getEntity().customName().toString();
            if (name.contains("Zombie de")) {
                e.setCancelled(true);
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
}
