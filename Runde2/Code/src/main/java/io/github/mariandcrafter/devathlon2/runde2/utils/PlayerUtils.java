package io.github.mariandcrafter.devathlon2.runde2.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * Utils for players.
 * Copied from round one of the DevAthlon.
 */
public final class PlayerUtils {

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

}
