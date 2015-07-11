package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Used to get notified when a runner runs nto the next base.
 */
public class PlayerMoveListener implements Listener {

    /**
     * Executed when a player moves.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;
        // check whether he moved to another block

        Player player = event.getPlayer();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null ||
                match.getCurrentPhase() != Match.Phase.RUNNING ||
                match.getRunnerPlayer() == player) return;
        // the player has to be in a match, he must be the runner and the current phase should be RUNNING

        int nextBase = match.nextBaseIndex(); // index of next base in the list of bases

        if (match.getGameMap().getBases().get(nextBase).getArea().containsBlockLocation(event.getTo())) {
            // he reached the next base, because he is in the area of the next base
            match.startInBase(nextBase);
        }

    }

}
