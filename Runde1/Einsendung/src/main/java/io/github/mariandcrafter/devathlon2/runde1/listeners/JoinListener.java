package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Used to notify the GameManager when a player joins the server, so the GameManager can do some things with the player.
 */
public class JoinListener implements Listener {

    /**
     * Executed when a player joins the server.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Main.getGameManager().onJoin(event.getPlayer());
    }

}
