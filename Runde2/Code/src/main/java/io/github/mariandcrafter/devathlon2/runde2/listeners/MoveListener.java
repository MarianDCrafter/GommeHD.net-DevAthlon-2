package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * Used to prevent players from moving in the phase STARTING. Also updates the compass of the runner when he moves and
 * checks whether the player moved into the void.
 */
public class MoveListener implements Listener {

    /**
     * Executed when a player moves.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // Check whether he moved to another block for performance reasons
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;

        Player player = event.getPlayer();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null) return;

        if (match.getPhase() == Match.Phase.STARTING) {
            // STARTING phase, moving not allowed:
            player.teleport(event.getFrom());
            return;
        }

        // Runner may has moved, update his compass
        if (match.getRunnerPlayer() == player)
            match.updateRunnerCompass();

        // Check whether the player is in the void and the phase is RUNNING:
        if (match.getGameMap().getMapVoid().getArea().containsBlockLocation(event.getTo()) &&
                match.getPhase() == Match.Phase.RUNNING) {

            suckOutPlayer(player, match.getGameMap().getMapVoid().getToLocation());

            if(match.getRunnerPlayer() == player)
                match.runnerFallingIntoVoid();
            else
                match.catcherFallingIntoVoid();
        }
    }

    /**
     * Sucks out a player into to the given location.
     * @param player the player to suck out
     * @param to the location
     */
    private void suckOutPlayer(Player player, Location to) {
        Location from = player.getLocation();

        // The play should be able to fly, because with this, the "sucking out" is working better and normally people
        // are not moving much in the sky.
        player.setAllowFlight(true);
        player.setFlying(true);

        // Suck out the player:
        player.setVelocity(new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ()).multiply(20));

        // The player is crying:
        PlayerUtils.playSound(from, Sound.GHAST_SCREAM, 100, 1);
    }

}
