package fr.yanis.superbecorpweapons.item.management;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class Item {

    HashMap<Player, Boolean> cooldown = new HashMap<>();
    HashMap<Player, CoolDownTask> cooldownTask = new HashMap<>();

    AItem annotation;

    public Item(){
       this.annotation = this.getClass().getAnnotation(AItem.class);
    }

    public abstract String getKey();
    public String getName(){
        if(ItemManager.getSection(getKey()) == null) return getAnnotation().defaultName();
        else return ItemManager.getSection(getKey()).getString("name");
    }

    public String getDescription(){
        if(ItemManager.getSection(getKey()) == null) return getAnnotation().defaultDescription();
        else return ItemManager.getSection(getKey()).getString("description");
    }

    public int getCooldown(){
        if(ItemManager.getSection(getKey()) == null) return getAnnotation().defaultCooldown();
        else return ItemManager.getSection(getKey()).getInt("cooldown");
    }

    public abstract ItemStack getItem();
    public abstract void onUse(PlayerInteractEvent e);

    public void onProjectileHit(ProjectileHitEvent e){}
    public void onAttackEntity(EntityDamageByEntityEvent e){}
    public void onEntityDeath(EntityDeathEvent e){}

    public void onDisable(){}

    public int getTimeLeft(Player player){
        return cooldownTask.get(player).getTime();
    }

    public void whenEntityIsTouchedByParticle(Entity entity){}

    public AItem getAnnotation() {
        return annotation;
    }
}
