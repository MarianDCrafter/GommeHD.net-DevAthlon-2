package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class MoveListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;
        // check whether he moved to another block

        Player player = event.getPlayer();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null) return;

        if (match.getPhase() == Match.Phase.STARTING) {
            player.teleport(event.getFrom());
            return;
        }

        if (match.getRunnerPlayer() == player)
            match.updateRunnerCompass();

        if ((match.getRunnerPlayer() == player || match.getCatcherPlayer() == player) &&
                match.getGameMap().getMapVoid().getArea().containsBlockLocation(event.getTo()) &&
                match.getPhase() == Match.Phase.RUNNING) {
            velocityPlayerToLocation(player, match.getGameMap().getMapVoid().getToLocation());

            if(match.getRunnerPlayer() == player)
                match.runnerFallingIntoVoid();
            else
                match.catcherFallingIntoVoid();
        }
    }

    private void velocityPlayerToLocation(Player player, Location to) {
        Location from = player.getLocation();
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setVelocity(new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ()).multiply(20));
    }

}
