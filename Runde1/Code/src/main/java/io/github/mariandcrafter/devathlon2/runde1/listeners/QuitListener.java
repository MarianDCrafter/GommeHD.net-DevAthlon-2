package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Used to notify the GameManager when a player joins the server, so the GameManager can do some things with the player.
 */
public class QuitListener implements Listener {

    /**
     * Executed when a player quits the server.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Main.getGameManager().onQuit(event.getPlayer());
    }

    /**
     * Executed when a player is kicked from the server.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerKickEvent event) {
        Main.getGameManager().onQuit(event.getPlayer());
    }

}
