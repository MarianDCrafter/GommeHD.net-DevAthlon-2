package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Main.getGameManager().onQuit(event.getPlayer());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerKickEvent event) {
        Main.getGameManager().onQuit(event.getPlayer());
    }

}
