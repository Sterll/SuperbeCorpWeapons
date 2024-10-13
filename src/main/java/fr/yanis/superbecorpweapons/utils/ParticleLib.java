package fr.yanis.superbecorpweapons.utils;

import com.destroystokyo.paper.ParticleBuilder;
import fr.yanis.superbecorpweapons.SCWMain;
import fr.yanis.superbecorpweapons.item.management.Item;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class ParticleLib {

    public static void spawnDome(Location location, Color color, double maxR, Item item) {
        new BukkitRunnable() {
            double radius = 0;
            double maxRadius = maxR;
            Collection<? extends Entity> entities = location.getWorld().getNearbyEntities(location, 15, 15, 15);
            @Override
            public void run() {
                if (radius > maxRadius) {
                    this.cancel();
                    return;
                }
                for (double y = 0; y <= radius; y += 0.2) {
                    double currentRadius = Math.sqrt(radius * radius - y * y);
                    for (int i = 0; i < 360; i += 10) {
                        double angle = Math.toRadians(i);
                        double x = currentRadius * Math.cos(angle);
                        double z = currentRadius * Math.sin(angle);
                        location.add(x, y, z);
                        new ParticleBuilder(Particle.DUST)
                                .data(new Particle.DustOptions(color, 1))
                                .location(location)
                                .count(2)
                                .spawn();
                        for (Entity entity : entities) {
                            if (entity.getWorld().equals(location.getWorld())
                                    && entity.getLocation().distanceSquared(location) <= 0.25) {
                                item.whenEntityIsTouchedByParticle(entity);
                            }
                        }
                        location.subtract(x, y, z);
                    }
                }
                radius += 0.2;
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 0L);
    }

    public static void spawnCircleAt(Location location, Color color, Item item){
        new BukkitRunnable() {
            double radius = 0;
            Collection<? extends Entity> entities = location.getWorld().getNearbyEntities(location, 15, 15, 15);
            @Override
            public void run() {
                if (radius > 5) {
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i);
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    location.add(x, 0, z);
                    new ParticleBuilder(Particle.DUST)
                            .data(new Particle.DustOptions(color, 1))
                            .location(location)
                            .count(2)
                            .spawn();
                    for (Entity entity : entities) {
                        if (entity.getWorld().equals(location.getWorld())
                                && entity.getLocation().distanceSquared(location) <= 0.25) {
                            item.whenEntityIsTouchedByParticle(entity);
                        }
                    }
                    location.subtract(x, 0, z);
                }
                radius += 0.2;
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 0L);
    }

    public static void spawnRandomParticles(Zombie zombie, Color color) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count > 20 || !zombie.isValid()) {
                    this.cancel();
                    return;
                }

                Location location = zombie.getLocation();

                for (int i = 0; i < 15; i++) {
                    double offsetX = (Math.random() - 0.5) * 1.5;
                    double offsetY = Math.random() * 1.5;
                    double offsetZ = (Math.random() - 0.5) * 1.5;

                    new ParticleBuilder(Particle.DUST)
                            .data(new Particle.DustOptions(color, 1))
                            .location(location)
                            .count(2)
                            .offset(offsetX, offsetY, offsetZ)
                            .spawn();
                }
                count++;
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 2L);
    }

    public static void spawnWaterWaves(Player player, Item item) {
        Collection<? extends Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), 15, 15, 15);

        Location startLocation = player.getLocation().clone().add(player.getLocation().getDirection().normalize().multiply(5));
        Color lightBlue = Color.fromRGB(173, 216, 230);
        Color darkBlue = Color.fromRGB(0, 0, 139);
        int waveWidth = 30;

        Vector direction = player.getLocation().getDirection().normalize();
        Vector perpendicular = new Vector(-direction.getZ(), 0, direction.getX()).normalize();

        new BukkitRunnable() {
            double waveOffset = 0;
            double heightOffset = 0;
            boolean goingUp = true;

            @Override
            public void run() {
                if (waveOffset > 10) {
                    this.cancel();
                    return;
                }
                if (goingUp) {
                    heightOffset += 0.1;
                    if (heightOffset >= 0.5) goingUp = false;
                } else {
                    heightOffset -= 0.1;
                    if (heightOffset <= 0) goingUp = true;
                }

                for (int widthOffset = -waveWidth / 2; widthOffset <= waveWidth / 2; widthOffset++) {
                    for (int i = 0; i < 10; i++) {
                        Vector offsetDirection = direction.clone().multiply(waveOffset - i * 0.3);
                        Vector offsetWidth = perpendicular.clone().multiply(widthOffset * 0.3);

                        Location particleLocation = startLocation.clone().add(offsetDirection).add(offsetWidth).add(0, heightOffset, 0);

                        // Crée une particule bleu clair pour la première ligne
                        new ParticleBuilder(Particle.DUST)
                                .data(new Particle.DustOptions(lightBlue, 1))
                                .location(particleLocation)
                                .count(1)
                                .spawn();
                        for (Entity entity : entities) {
                            if (entity.getWorld().equals(particleLocation.getWorld())
                                    && entity.getLocation().distanceSquared(particleLocation) <= 0.75) {
                                item.whenEntityIsTouchedByParticle(entity);
                            }
                        }

                        particleLocation.add(perpendicular.clone().multiply(0.3));
                        new ParticleBuilder(Particle.DUST)
                                .data(new Particle.DustOptions(darkBlue, 1))
                                .location(particleLocation)
                                .count(1)
                                .spawn();
                        for (Entity entity : entities) {
                            if (entity.getWorld().equals(particleLocation.getWorld())
                                    && entity.getLocation().distanceSquared(particleLocation) <= 0.75) {
                                item.whenEntityIsTouchedByParticle(entity);
                            }
                        }
                    }
                }
                waveOffset += 0.2;
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 2L);
    }

    public static void spawnRotatingCircle(Entity entity, Color color) {
        new BukkitRunnable() {
            double angle = 0;
            int duration = 0;

            @Override
            public void run() {
                if (!entity.isValid() || duration > 50) {
                    this.cancel();
                    return;
                }

                Location center = entity.getLocation().add(0, 1, 0);
                double radius = 1.5;

                for (int i = 0; i < 360; i += 20) {
                    double radianAngle = Math.toRadians(i + angle);
                    double x = radius * Math.cos(radianAngle);
                    double z = radius * Math.sin(radianAngle);

                    Location particleLocation = center.clone().add(x, 0, z);
                    new ParticleBuilder(Particle.DUST)
                            .data(new Particle.DustOptions(color, 1))
                            .location(particleLocation)
                            .count(1)
                            .spawn();
                }

                angle += 10;
                if (angle >= 360) angle = 0;

                duration++;
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 2L);
    }
}
