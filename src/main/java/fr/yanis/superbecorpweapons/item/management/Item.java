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

public abstract class Item {

    public abstract String getKey();
    public abstract String getName();
    public abstract String getDescription();
    public abstract ItemStack getItem();

    public abstract void onUse(PlayerInteractEvent e);
    public void onProjectileHit(ProjectileHitEvent e){}
    public void onUseAtEntity(PlayerInteractAtEntityEvent e){}
    public void onAttackEntity(EntityDamageByEntityEvent e){}

    public void onMove(PlayerMoveEvent e){}
    public void onEntityDeath(EntityDeathEvent e){}

    public void whenEntityIsTouchedByParticle(Entity entity){}

}
