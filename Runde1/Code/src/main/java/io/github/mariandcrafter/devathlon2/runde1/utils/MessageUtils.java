package io.github.mariandcrafter.devathlon2.runde1.utils;

import org.bukkit.ChatColor;

/**
 * Utils for messages that can be sent to players.
 */
public final class MessageUtils {

    /**
     * Creates a new string with the prefix and the given message.
     *
     * @param message the message to use
     * @return the created string
     */
    public static String message(String message) {
        return ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + ChatColor.BOLD + "DevAthlon" + ChatColor.GRAY + "] " + message;
    }

}
