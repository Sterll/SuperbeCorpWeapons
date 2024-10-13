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
        }.runTaskTimer(SCWMain.getInstance(), 0L, 2L); // Exécute toutes les 2 ticks
    }

    public static void spawnWaterWaves(Player player, Item item) {
        Collection<? extends Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), 15, 15, 15);

        // On clone la position du joueur et on applique un décalage initial de 2 blocs en direction de son regard
        Location startLocation = player.getLocation().clone().add(player.getLocation().getDirection().normalize().multiply(5));
        Color lightBlue = Color.fromRGB(173, 216, 230); // Bleu clair
        Color darkBlue = Color.fromRGB(0, 0, 139);      // Bleu foncé
        int waveWidth = 30; // Largeur de la vague en blocs

        // Direction dans laquelle le joueur regarde
        Vector direction = player.getLocation().getDirection().normalize();
        Vector perpendicular = new Vector(-direction.getZ(), 0, direction.getX()).normalize(); // Perpendiculaire à la direction

        new BukkitRunnable() {
            double waveOffset = 0; // Offset de progression de la vague
            double heightOffset = 0;
            boolean goingUp = true;

            @Override
            public void run() {
                if (waveOffset > 10) { // Limite la longueur de la vague
                    this.cancel();
                    return;
                }

                // Calcule la position verticale en fonction de l’oscillation
                if (goingUp) {
                    heightOffset += 0.1;
                    if (heightOffset >= 0.5) goingUp = false;
                } else {
                    heightOffset -= 0.1;
                    if (heightOffset <= 0) goingUp = true;
                }

                // Crée des rangées de particules pour chaque couleur sur la largeur de la vague
                for (int widthOffset = -waveWidth / 2; widthOffset <= waveWidth / 2; widthOffset++) {
                    for (int i = 0; i < 10; i++) { // Longueur de la vague
                        // Décalage dans la direction du joueur pour contrôler l'avancement
                        Vector offsetDirection = direction.clone().multiply(waveOffset - i * 0.3);

                        // Décalage en largeur de la vague (perpendiculaire à la direction du joueur)
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
                        // Crée une particule bleu foncé pour la deuxième ligne
                        particleLocation.add(perpendicular.clone().multiply(0.3)); // Décalage supplémentaire pour créer la double ligne
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
                waveOffset += 0.2; // Fait avancer la vague progressivement dans la direction du joueur
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 2L); // Exécution toutes les 2 ticks pour un mouvement fluide
    }

    public static void spawnRotatingCircle(Entity entity, Color color) {
        new BukkitRunnable() {
            double angle = 0; // Angle de rotation initial
            int duration = 0; // Compteur pour arrêter l'animation après 3 secondes (60 ticks)

            @Override
            public void run() {
                if (!entity.isValid() || duration > 50) { // Annule la tâche si l'entité n'est plus valide ou après 3 secondes
                    this.cancel();
                    return;
                }

                Location center = entity.getLocation().add(0, 1, 0); // Centre du cercle, légèrement au-dessus du sol
                double radius = 1.5; // Rayon ajusté pour être près de l'entité

                for (int i = 0; i < 360; i += 20) { // Particules espacées tous les 20 degrés pour plus de densité
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

                angle += 10; // Incrémente l'angle pour faire tourner le cercle
                if (angle >= 360) angle = 0; // Réinitialise l'angle après une rotation complète

                duration++; // Incrémente le compteur de durée
            }
        }.runTaskTimer(SCWMain.getInstance(), 0L, 2L); // Exécution toutes les 2 ticks pour une rotation fluide
    }
}
