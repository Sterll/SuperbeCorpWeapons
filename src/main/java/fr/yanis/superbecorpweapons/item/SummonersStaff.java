package fr.yanis.superbecorpweapons.item;

import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

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
        Zombie zombie = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), Zombie.class);
        zombie.setCustomName("§cZombie de §b" + e.getPlayer().getName());
        zombie.setCustomNameVisible(true);
        zombie.setTarget(null);
        zombie.setAdult();
        zombie.setAI(false);
        addZombie(e.getPlayer(), zombie);
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
    public void onMove(PlayerMoveEvent e) {
        if(zombies.containsKey(e.getPlayer())) {
            for(Zombie zombie : zombies.get(e.getPlayer())) {
                zombie.getPathfinder().moveTo(e.getPlayer());
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
