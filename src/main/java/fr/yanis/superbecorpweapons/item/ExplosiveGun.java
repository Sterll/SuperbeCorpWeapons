package fr.yanis.superbecorpweapons.item;

import com.destroystokyo.paper.ParticleBuilder;
import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import fr.yanis.superbecorpweapons.utils.ParticleLib;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ExplosiveGun extends Item {
    @Override
    public String getKey() {
        return "explosive_gun";
    }

    @Override
    public String getName() {
        return "§cPistolet Explosif";
    }

    @Override
    public String getDescription() {
        return "§bC'est juste un pistolet qui fait boum";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .build();
    }

    @Override
    public int getCooldown() {
        return 5;
    }

    @Override
    public void onUse(PlayerInteractEvent e) {
        Chicken entity = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), Chicken.class);
        entity.customName(Component.text("§cBooooooom dans §b5 §csecondes"));
        entity.setCustomNameVisible(true);
        entity.setInvulnerable(true);
        entity.setGlowing(true);
        entity.setVelocity(e.getPlayer().getLocation().getDirection().multiply(2));
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);

        new BukkitRunnable(){
            int time = 5;
            @Override
            public void run() {
                entity.customName(Component.text("§cBooooooom dans §b" + time + " §csecondes"));
                if(time == 0){
                    Location location = entity.getLocation();
                    entity.remove();
                    e.getPlayer().getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    ParticleLib.spawnDome(location, Color.fromRGB(0,255,0), 5, ExplosiveGun.this);
                    cancel();
                    return;
                }
                time--;
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 20L);

    }

    @Override
    public void whenEntityIsTouchedByParticle(Entity entity) {
        if(!(entity instanceof LivingEntity)) return;
        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 1));
    }
}
