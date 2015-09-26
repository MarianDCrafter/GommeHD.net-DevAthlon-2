package io.github.mariandcrafter.devathlon2.runde3.listeners;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.game.Game;
import io.github.mariandcrafter.devathlon2.runde3.game.Gamemode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if ((event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                event.getFrom().getBlockZ() != event.getTo().getBlockZ()) &&
                Main.getGameManager().getGames().containsKey(event.getPlayer().getUniqueId())) {
            Game game = Main.getGameManager().getGames().get(event.getPlayer().getUniqueId());
            Gamemode gamemode = game.getGamemode();
            if (!gamemode.getArea().containsBlockLocation(event.getTo())) {
                game.playerLeftArea();
            }
        }
    }

}
