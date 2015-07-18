package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

/**
 * A rescue capsule of a map.
 */
public class RescueCapsule {

    /**
     * The material of the entrance/exit.
     */
    private Material entranceMaterial, exitMaterial;
    /**
     * The area of the entrance/exit. Filled with the material when closed.
     */
    private Area entranceArea, exitArea;
    /**
     * The location of the rescue button.
     */
    private Location rescueButtonLocation;

    /**
     * The current time of the task.
     */
    private int time;
    /**
     * The task used by the rescue capsule.
     */
    private BukkitTask task;

    /**
     * Creates a new rescue capsule
     * @param entranceMaterial the material of the entrance
     * @param entranceArea the area of the entrance
     * @param exitMaterial the material of the exit
     * @param exitArea the area of the exit
     * @param rescueButtonLocation the location of the rescue button
     */
    public RescueCapsule(Material entranceMaterial, Area entranceArea, Material exitMaterial, Area exitArea, Location rescueButtonLocation) {
        this.entranceMaterial = entranceMaterial;
        this.entranceArea = entranceArea;
        this.exitMaterial = exitMaterial;
        this.exitArea = exitArea;
        this.rescueButtonLocation = rescueButtonLocation;
    }

    /**
     * Opens the entrance and fills the area with air.
     */
    public void openEntrance() {
        entranceArea.fill(Material.AIR);
    }

    /**
     * Closes the entrance and fills the area with the entrance material.
     */
    public void closeEntrance() {
        entranceArea.fill(entranceMaterial);
    }

    /**
     * Opens the exit and fills the area with air.
     */
    public void openExit() {
        exitArea.fill(Material.AIR);
    }

    /**
     * Closes the exit and fills the area with the exit material.
     */
    public void closeExit() {
        exitArea.fill(exitMaterial);
    }

    /**
     * @return the location of the rescue button
     */
    public Location getRescueButtonLocation() {
        return rescueButtonLocation;
    }

    /**
     * Stops the current task if running.
     */
    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    /**
     * Called when a player used the rescue capsule. Used to close the entrance, open the exit and play a sound.
     * @param player the player who used the rescue capsule
     */
    private void usedByPlayer(Player player) {
        closeEntrance();
        openExit();

        PlayerUtils.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 50, 1);
    }

    /**
     * Called when a player used the rescue capsule while wearing the whole armor. Creates effects and starts the task.
     * @param player the player who used the rescue capsule
     */
    public void usedByPlayerWithArmor(final Player player) {
        usedByPlayer(player);

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 80));

        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                task = null;

                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 40, 1));
            }
        }, 20L);
    }

    /**
     * Called when a player used the rescue capsule while wearing not the whole armor. Creates effects and starts the task.
     * @param player the player who used the rescue capsule
     */
    public void usedByPlayerWithoutArmor(final Player player) {
        usedByPlayer(player);

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 80));

        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 40, 1));
                runnerUsedRescueCapsuleWithoutFullArmorStartDamage(player);
            }
        }, 20L);
    }

    /**
     * Called when a player should start to get damage for two seconds.
     * @param player the player who should start to get damage
     */
    private void runnerUsedRescueCapsuleWithoutFullArmorStartDamage(final Player player) {
        time = 40;
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                time--;
                if (time <= 0) {
                    task.cancel();
                    task = null;
                } else {
                    player.damage(0);
                }
            }
        }, 1L, 1L);
    }

}
