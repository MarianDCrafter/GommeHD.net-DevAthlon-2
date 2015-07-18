package io.github.mariandcrafter.devathlon2.runde2.utils;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * Utils for players.
 * Parts are copied from round one of the DevAthlon.
 */
public final class PlayerUtils {

    private PlayerUtils() {
    }

    /**
     * Clears the given player. Clears inventory, sets fire ticks to 0, resets health, disable fly mode etc.
     * @param player the player to clear
     */
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

    /**
     * Plays the given sound to all players at THEIR location.
     * @param sound the sound to send
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     * @param players the players to send the sound
     */
    public static void playSound(Sound sound, float volume, float pitch, Player... players) {
        for (Player player : players)
            player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Plays the given sound at the given location to all players.
     * @param location where to play the sound
     * @param sound the sound
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public static void playSound(Location location, Sound sound, float volume, float pitch) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    /**
     * Plays the given effect to all given players at the given location.
     * @param location where to play the effect
     * @param effect the effect to play
     * @param data optional data of the effect
     * @param players the players to send the effect
     * @param <T> the type of the data
     */
    public static <T> void playEffect(Location location, Effect effect, T data, Player... players) {
        for (Player player : players)
            location.getWorld().playEffect(location, effect, data);
    }

    /**
     * Plays the given effect at the given location to all players.
     * @param location where to play the effect
     * @param effect the effect to play
     * @param data optional data of the effect
     * @param <T> the type of the data
     */
    public static <T> void playEffect(Location location, Effect effect, T data) {
        location.getWorld().playEffect(location, effect, data);
    }

}
