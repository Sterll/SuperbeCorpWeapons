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
        }.runTaskTimer(SCWMain.getInstance(), 0L, 2L); // Exécute toutes les 2 ticks
    }

}
