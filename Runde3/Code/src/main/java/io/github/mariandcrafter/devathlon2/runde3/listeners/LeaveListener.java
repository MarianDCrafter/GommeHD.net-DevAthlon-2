package io.github.mariandcrafter.devathlon2.runde3.listeners;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        onLeave(event.getPlayer());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
        onLeave(event.getPlayer());
    }

    private void onLeave(Player player) {
        Main.getGameManager().removePlayerFromGame(player);
    }

}
