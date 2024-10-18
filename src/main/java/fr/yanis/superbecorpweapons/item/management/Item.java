package fr.yanis.superbecorpweapons.item.management;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public abstract class Item {

    HashMap<UUID, Boolean> cooldown = new HashMap<>();
    HashMap<UUID, CoolDownTask> cooldownTask = new HashMap<>();

    AItem annotation;

    public Item(){
       this.annotation = this.getClass().getAnnotation(AItem.class);
    }

    public abstract String getKey();

    @NotNull
    public String getName(){
        if(ItemManager.getSection(getKey()) == null || ItemManager.getSection(getKey()).getString("name") == null) return getAnnotation().defaultName();
        else return ItemManager.getSection(getKey()).getString("name");
    }

    @NotNull
    public String getDescription(){
        if(ItemManager.getSection(getKey()) == null || ItemManager.getSection(getKey()).getString("description") == null) return getAnnotation().defaultDescription();
        else return ItemManager.getSection(getKey()).getString("description");
    }

    public int getCooldown(){
        if(ItemManager.getSection(getKey()) == null) return getAnnotation().defaultCooldown();
        else return ItemManager.getSection(getKey()).getInt("cooldown");
    }

    @NotNull
    public abstract ItemStack getItem();
    public abstract byte getID();
    public abstract void onUse(@NotNull PlayerInteractEvent e, @NotNull ItemManager itemManager);

    @MustBeInvokedByOverriders
    public void onQuit(PlayerQuitEvent e){
        cooldown.remove(e.getPlayer().getUniqueId());
        cooldownTask.remove(e.getPlayer().getUniqueId());
    }

    public void onProjectileHit(@NotNull ProjectileHitEvent e){}
    public void onAttackEntity(@NotNull EntityDamageByEntityEvent e){}

    public void onDisable(){}

    public int getTimeLeft(@NotNull UUID uuid){
        return cooldownTask.get(uuid).getTime();
    }

    public void whenEntityIsTouchedByParticle(@NotNull Entity entity, @NotNull ItemManager itemManager){}

    @NotNull
    public AItem getAnnotation() {
        return annotation;
    }
}
