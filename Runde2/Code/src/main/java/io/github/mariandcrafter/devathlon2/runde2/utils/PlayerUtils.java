package io.github.mariandcrafter.devathlon2.runde2.utils;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * Utils for players.
 * Copied from round one of the DevAthlon.
 */
public final class PlayerUtils {

    private PlayerUtils() {
    }

    public static void clear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        player.setAllowFlight(false);
        player.setBedSpawnLocation(null);
        player.setExp(0);
        player.setFlying(false);
        player.setFoodLevel(20);
        player.setHealthScaled(false);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.setCanPickupItems(false);
        player.resetMaxHealth();
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public static void playSound(Sound sound, float v, float v1, Player... players) {
        for (Player player : players)
            player.playSound(player.getLocation(), sound, v, v1);
    }

    public static void playSound(Location location, Sound sound, float v, float v1) {
        location.getWorld().playSound(location, sound, v, v1);
    }

    public static <T> void playEffect(Location location, Effect effect, T data, Player... players) {
        for (Player player : players)
            location.getWorld().playEffect(location, effect, data);
    }

    public static <T> void playEffect(Location location, Effect effect, T data) {
        location.getWorld().playEffect(location, effect, data);
    }

}
