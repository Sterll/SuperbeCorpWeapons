package fr.yanis.superbecorpweapons.item;

import com.destroystokyo.paper.ParticleBuilder;
import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.AItem;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import fr.yanis.superbecorpweapons.item.management.config.IntConfig;
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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@AItem(defaultName = "§cPistolet Explosif", defaultDescription = "§bC'est juste un pistolet qui fait boum", defaultCooldown = 5, intConfigValues = {
        @IntConfig(name = "chicken_time", value = 5),
        @IntConfig(name = "push_power", value = 2),
        @IntConfig(name = "poison_time", value = 5)
})
public class ExplosiveGun extends Item {

    public ExplosiveGun(){
        super();
    }

    @Override
    public String getKey() {
        return "explosive_gun";
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .setCustomModelData(28)
                .addPersistantData(ItemManager.key, PersistentDataType.BYTE, getID())
                .build();
    }

    @Override
    public byte getID() {
        return 1;
    }

    @Override
    public void onUse(@NotNull PlayerInteractEvent e, @NotNull ItemManager itemManager) {

        Chicken entity = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), Chicken.class);

        entity.customName(Component.text("§cBooooooom dans §b" +
                Objects.requireNonNull(itemManager.getSection(),
                        "La configuration pour le pouletn n'est pas trouvable")
                        .getString("chicken_time") +  "§csecondes"));

        entity.setCustomNameVisible(true);
        entity.setInvulnerable(true);
        entity.setGlowing(true);
        entity.setVelocity(e.getPlayer().getLocation().getDirection().multiply(itemManager.getSection().getInt("push_power")));
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED), "L'attribut GENERIC_MOVEMENT_SPEED n'a pu être trouvé").setBaseValue(0);

        new BukkitRunnable(){
            int time = Integer.parseInt(Objects.requireNonNull(itemManager.getSection().getString("cooldown"),
                    "La configuration pour le poulet n'est pas trouvable"));

            @Override
            public void run() {
                entity.customName(Component.text("§cBooooooom dans §b" + time + " §csecondes"));
                if(time == 0){
                    Location location = entity.getLocation();
                    entity.remove();

                    e.getPlayer().playSound(location, "minecraft:custom.explosive_sound", 1.0f, 1.0f);
                    ParticleLib.spawnDome(location, Color.fromRGB(0,255,0), 5, itemManager);

                    cancel();

                    return;
                }

                time--;
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 20L);

    }

    @Override
    public void whenEntityIsTouchedByParticle(@NotNull Entity entity, @NotNull ItemManager itemManager) {
        if(!(entity instanceof LivingEntity))
            return;

        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * Objects.requireNonNull(itemManager.getSection(),
                "La configuration pour le pouletn n'est pas trouvable")
                .getInt("poison_time"), 1));
    }
}
