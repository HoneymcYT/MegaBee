package de.simpmc.honeysMegaBiene;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class HoneysMegaBiene extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("Honeybee").setExecutor(new HoneyBeeCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public class HoneyBeeCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                World world = player.getWorld();
                Location location = player.getLocation();

                // Biene spawnen
                Bee bee = world.spawn(location, Bee.class);
                bee.setCustomName("Megabee");
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

                        timeLeft -= 10; // Decrease timeLeft in each iteration
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

                return true;
            }

            return false;
        }
    }
}
