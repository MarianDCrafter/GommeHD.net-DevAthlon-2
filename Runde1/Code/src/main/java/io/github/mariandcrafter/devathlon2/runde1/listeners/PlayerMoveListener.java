package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;

        Player player = event.getPlayer();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null) return;

        if (match.getCurrentPhase() == Match.Phase.RUNNING && match.getRunnerPlayer() == player) {

            int nextBase = match.nextBaseIndex();

            if (match.getGameMap().getBases().get(nextBase).getArea().containsBlockLocation(event.getTo())) {
                match.startInBase(nextBase);
            }
        }

    }

}
