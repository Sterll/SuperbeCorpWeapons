package fr.yanis.superbecorpweapons.item;

import com.destroystokyo.paper.ParticleBuilder;
import fr.yanis.superbecorpweapons.item.management.AItem;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import fr.yanis.superbecorpweapons.utils.ParticleLib;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

@AItem(defaultName = "§aPistolet Laser", defaultDescription = "§bC'est juste un pistolet laser qui rebondit", defaultCooldown = 2)
public class LaserGun extends Item {

    private HashSet<UUID> alreadyHit = new HashSet<>();

    public LaserGun(){
        super();
    }

    @Override
    public String getKey() {
        return "lasergun";
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .setCustomModelData(29)
                .addPersistantData(ItemManager.key, PersistentDataType.BYTE, getID())
                .build();
    }

    @Override
    public byte getID() {
        return 3;
    }

    @Override
    public void onUse(@NotNull PlayerInteractEvent e, @NotNull ItemManager itemManager) {
        this.createLaser(e.getPlayer(), e.getPlayer().getEyeLocation(), e.getPlayer().getLocation().getDirection(), e.getPlayer().getWorld(), 10, 50, itemManager);

        e.getPlayer().playSound(e.getPlayer().getLocation(), "minecraft:custom.raygun_sound", 1.0f, 1.0f);
    }

    @Override
    public void whenEntityIsTouchedByParticle(@NotNull Entity entity, @NotNull ItemManager itemManager) {
        if(this.alreadyHit.contains(entity.getUniqueId()))
            return;

        entity.getWorld().strikeLightning(entity.getLocation());
        this.alreadyHit.add(entity.getUniqueId());
    }

    public void createLaser(Player attacker, Location origin, Vector direction, World world, int maxReflections, double distance, ItemManager itemManager) {
        Location currentPos = origin.clone();
        Vector currentDirection = direction.clone().normalize();
        int reflections = 0;
        double traveledDistance = 0;

        while (reflections <= maxReflections && traveledDistance < distance) {
            currentPos.add(currentDirection.clone().multiply(0.1));
            traveledDistance += 0.1;

            new ParticleBuilder(Particle.DUST)
                    .data(new Particle.DustOptions(Color.RED, 1))
                    .location(currentPos)
                    .count(1)
                    .extra(0)
                    .spawn();

            currentPos.getNearbyEntities(0.1, 0.1, 0.1).forEach(entity -> {
                if (entity == attacker) return;
                this.whenEntityIsTouchedByParticle(entity, itemManager);
            });

            Block hitBlock = currentPos.getBlock();

            if (!hitBlock.isPassable()) {
                BlockFace hitFace = this.getHitBlockFace(currentPos, hitBlock);
                if (hitFace != BlockFace.SELF) {
                    Vector normal = getBlockNormal(hitFace);
                    currentDirection = this.calculateReflection(currentDirection, normal);
                    reflections++;
                }
            }
        }
    }

    private Vector calculateReflection(Vector incoming, Vector normal) {
        return incoming.subtract(normal.multiply(2 * incoming.dot(normal))).normalize();
    }

    private Vector getBlockNormal(BlockFace face) {
        switch (face) {
            case NORTH: return new Vector(0, 0, -1);
            case SOUTH: return new Vector(0, 0, 1);
            case WEST:  return new Vector(-1, 0, 0);
            case EAST:  return new Vector(1, 0, 0);
            case UP:    return new Vector(0, 1, 0);
            case DOWN:  return new Vector(0, -1, 0);
            default:    return new Vector(0, 0, 0);
        }
    }

    private BlockFace getHitBlockFace(Location location, Block block) {
        Location blockCenter = block.getLocation().add(0.5, 0.5, 0.5);
        Vector relative = location.toVector().subtract(blockCenter.toVector());

        double absX = Math.abs(relative.getX());
        double absY = Math.abs(relative.getY());
        double absZ = Math.abs(relative.getZ());

        if (absX > absY && absX > absZ) {
            return relative.getX() > 0 ? BlockFace.EAST : BlockFace.WEST;
        } else if (absY > absX && absY > absZ) {
            return relative.getY() > 0 ? BlockFace.UP : BlockFace.DOWN;
        } else {
            return relative.getZ() > 0 ? BlockFace.SOUTH : BlockFace.NORTH;
        }
    }

}
