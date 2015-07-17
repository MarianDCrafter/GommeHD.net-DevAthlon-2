package io.github.mariandcrafter.devathlon2.runde2.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Utils for messages that can be sent to players.
 */
public final class MessageUtils {

    /**
     * Sends the given player an error message
     *
     * @param player the player to send the message
     * @param message the message to use
     */
    public static void error(Player player, String message) {
        player.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + ChatColor.BOLD + "DevAthlon" + ChatColor.GRAY + "] " + ChatColor.RED + message);
    }

    /**
     * Sends the given player an info message
     *
     * @param player the player to send the message
     * @param message the message to use
     */
    public static void info(Player player, String message) {
        player.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + ChatColor.BOLD + "DevAthlon" + ChatColor.GRAY + "] " + ChatColor.GOLD + message);
    }

}
