package fr.yanis.superbecorpweapons.item.management;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public abstract class Item {

    HashMap<Player, Boolean> cooldown = new HashMap();
    HashMap<Player, CoolDownTask> cooldownTask = new HashMap();

    public abstract String getKey();
    public abstract String getName();
    public abstract String getDescription();
    public abstract ItemStack getItem();
    public abstract int getCooldown();

    public abstract void onUse(PlayerInteractEvent e);
    public void onProjectileHit(ProjectileHitEvent e){}
    public void onUseAtEntity(PlayerInteractAtEntityEvent e){}
    public void onAttackEntity(EntityDamageByEntityEvent e){}

    public void onEntityDeath(EntityDeathEvent e){}

    public void onDisable(){}

    public int getTimeLeft(Player player){
        return cooldownTask.get(player).getTime();
    }

    public void whenEntityIsTouchedByParticle(Entity entity){}

}
