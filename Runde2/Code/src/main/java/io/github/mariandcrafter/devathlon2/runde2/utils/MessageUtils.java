package io.github.mariandcrafter.devathlon2.runde2.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Utils for messages that can be sent to players.
 */
public final class MessageUtils {

    private MessageUtils() {}

    private static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + ChatColor.BOLD + "DevAthlon" + ChatColor.GRAY + "] ";

    /**
     * Sends the given player an error message
     *
     * @param message the message to use
     * @param players the players to send the message
     */
    public static void error(String message, Player... players) {
        for (Player player : players)
            player.sendMessage(PREFIX + ChatColor.RED + message);
    }

    /**
     * Sends the given player an info message
     *
     * @param message the message to use
     * @param players the players to send the message
     */
    public static void info(String message, Player... players) {
        for (Player player : players)
            player.sendMessage(PREFIX + ChatColor.GOLD + message);
    }

    /**
     * Sends the given player a success message
     *
     * @param message the message to use
     * @param players the players to send the message
     */
    public static void success(String message, Player... players) {
        for (Player player : players)
            player.sendMessage(PREFIX + ChatColor.GREEN + message);
    }

}
