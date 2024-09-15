package de.simpmc.honeysMegaBiene;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class HoneysMegaBiene extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this); // EventHandler registrieren
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        // Überprüfen, ob der Befehl "/HoneyBee" ist
        if (event.getMessage().equalsIgnoreCase("/HoneyBee")) {
            Player player = event.getPlayer();
            World world = player.getWorld();
            Location location = player.getLocation();

            // Biene spawnen
            Bee bee = world.spawn(location, Bee.class);
            bee.setCustomName("MegaBiene");
            bee.setCustomNameVisible(true);

            // Schwebende Biene durch Bewegung und Partikeleffekte
            new BukkitRunnable() {
                int timeLeft = 30 * 20; // 30 Sekunden (20 Ticks pro Sekunde)

                @Override
                public void run() {
                    if (timeLeft <= 0 || bee.isDead()) {
                        bee.remove(); // Entferne die Biene nach 30 Sekunden
                        cancel();
                        return;
                    }

                    // Setzt die Y-Koordinate leicht an, um die Biene schweben zu lassen
                    Location beeLocation = bee.getLocation();
                    beeLocation.setY(beeLocation.getY() + 0.1);
                    bee.teleport(beeLocation);

                    // Erzeugt Partikel (Herz-Effekte)
                    world.spawnParticle(Particle.HEART, beeLocation, 5);

                    timeLeft -= 10; // Verringere timeLeft bei jedem Durchlauf
                }
            }.runTaskTimer(HoneysMegaBiene.this, 0L, 10L); // 10 Ticks zwischen jedem Lauf (0,5 Sekunden)

            // Entferne die Biene nach 30 Sekunden
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!bee.isDead()) {
                        bee.remove();
                    }
                }
            }.runTaskLater(HoneysMegaBiene.this, 30 * 20L); // 30 Sekunden Verzögerung

            event.setCancelled(true); // Verhindert, dass eine unbekannte Fehlermeldung ausgegeben wird
        }
    }
}
