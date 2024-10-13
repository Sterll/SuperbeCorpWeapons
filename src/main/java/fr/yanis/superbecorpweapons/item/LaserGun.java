package fr.yanis.superbecorpweapons.item;

import com.destroystokyo.paper.ParticleBuilder;
import fr.yanis.superbecorpweapons.item.management.Item;
import fr.yanis.superbecorpweapons.utils.ItemBuilder;
import fr.yanis.superbecorpweapons.utils.ParticleLib;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class LaserGun extends Item {
    @Override
    public String getKey() {
        return "lasergun";
    }

    @Override
    public String getName() {
        return "§aPistolet Laser";
    }

    @Override
    public String getDescription() {
        return "§bC'est juste un pistolet laser qui rebondit";
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
        createLaser(e.getPlayer().getEyeLocation(), e.getPlayer().getLocation().getDirection(), e.getPlayer().getWorld(), 10, 50);
    }

    @Override
    public void whenEntityIsTouchedByParticle(Entity entity) {
        entity.getWorld().strikeLightning(entity.getLocation());
    }

    public void createLaser(Location origin, Vector direction, World world, int maxReflections, double distance) {
        Location currentPos = origin.clone();
        Vector currentDirection = direction.clone().normalize(); // Initial direction of the laser
        int reflections = 0;
        double traveledDistance = 0;

        while (reflections <= maxReflections && traveledDistance < distance) {
            // Avance le laser sans modifier currentDirection lui-même
            currentPos.add(currentDirection.clone().multiply(0.1));
            traveledDistance += 0.1;

            // Spawn particle at the current location
            new ParticleBuilder(Particle.DUST)
                    .data(new Particle.DustOptions(Color.RED, 1))
                    .location(currentPos)
                    .count(1)
                    .extra(0)
                    .spawn();
            currentPos.getNearbyEntities(0.5, 0.5, 0.5).forEach(this::whenEntityIsTouchedByParticle);

            Block hitBlock = currentPos.getBlock();
            if (!hitBlock.isPassable()) {  // If block is not air or a passable block
                // Calculate reflection
                BlockFace hitFace = getHitBlockFace(currentPos, hitBlock);
                if (hitFace != BlockFace.SELF) { // Ignore invalid faces
                    Vector normal = getBlockNormal(hitFace);  // Get block face normal
                    currentDirection = calculateReflection(currentDirection, normal);
                    reflections++;
                }
            }
        }
    }

    // Calculate reflection using the block's normal vector
    private Vector calculateReflection(Vector incoming, Vector normal) {
        // Reflection formula: R = D - 2 * (D · N) * N
        return incoming.subtract(normal.multiply(2 * incoming.dot(normal))).normalize();
    }

    // Get the normal vector based on the BlockFace
    private Vector getBlockNormal(BlockFace face) {
        switch (face) {
            case NORTH: return new Vector(0, 0, -1);  // Negative Z
            case SOUTH: return new Vector(0, 0, 1);   // Positive Z
            case WEST:  return new Vector(-1, 0, 0);  // Negative X
            case EAST:  return new Vector(1, 0, 0);   // Positive X
            case UP:    return new Vector(0, 1, 0);   // Positive Y
            case DOWN:  return new Vector(0, -1, 0);  // Negative Y
            default:    return new Vector(0, 0, 0);   // Default case, should never happen
        }
    }

    // Approximate a method to get the hit BlockFace based on the player's current position and block
    private BlockFace getHitBlockFace(Location location, Block block) {
        // This is a simple version of the method that can be refined:
        // You could use Spigot's ray tracing API to get a more accurate hit result.
        Location blockCenter = block.getLocation().add(0.5, 0.5, 0.5);  // Center of the block
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

    private BlockFace getHitBlockFaceV2(Location location, Block block) {
        RayTraceResult result = block.getWorld().rayTraceBlocks(location, location.getDirection(), 1.0);
        return result != null ? result.getHitBlockFace() : BlockFace.SELF;
    }
}
