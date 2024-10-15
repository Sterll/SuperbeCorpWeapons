package fr.yanis.superbecorpweapons.item;

import com.destroystokyo.paper.ParticleBuilder;
import fr.yanis.superbecorpweapons.item.management.AItem;
import fr.yanis.superbecorpweapons.item.management.Item;
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
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

@AItem(defaultName = "§aPistolet Laser", defaultDescription = "§bC'est juste un pistolet laser qui rebondit", defaultCooldown = 2)
public class LaserGun extends Item {

    public LaserGun(){
        super();
    }

    @Override
    public String getKey() {
        return "lasergun";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STICK)
                .setName(Component.text(getName()))
                .addLore(Component.text("§f")).addLore(Component.text(getDescription()))
                .setCustomModelData(29)
                .build();
    }

    @Override
    public void onUse(PlayerInteractEvent e) {
        createLaser(e.getPlayer(), e.getPlayer().getEyeLocation(), e.getPlayer().getLocation().getDirection(), e.getPlayer().getWorld(), 10, 50);
        e.getPlayer().playSound(e.getPlayer().getLocation(), "minecraft:custom.raygun_sound", 1.0f, 1.0f);
    }

    @Override
    public void whenEntityIsTouchedByParticle(Entity entity) {
        entity.getWorld().strikeLightning(entity.getLocation());
    }

    public void createLaser(Player attacker, Location origin, Vector direction, World world, int maxReflections, double distance) {
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
            currentPos.getNearbyEntities(0.5, 0.5, 0.5).forEach(entity -> {
                if (entity == attacker) return;
                whenEntityIsTouchedByParticle(entity);
            });

            Block hitBlock = currentPos.getBlock();
            if (!hitBlock.isPassable()) {
                BlockFace hitFace = getHitBlockFace(currentPos, hitBlock);
                if (hitFace != BlockFace.SELF) {
                    Vector normal = getBlockNormal(hitFace);
                    currentDirection = calculateReflection(currentDirection, normal);
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
