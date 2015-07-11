package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Used to prevent the players from PvP and other damage (only in lobby).
 * This class also checks whether the runner hits the catcher with the arrow.
 */
public class PlayerDamageListener implements Listener {

    /**
     * Executed when an entity gets damaged.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return; // we only want to listen to damaged players
        Player player = (Player) event.getEntity();

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;

            if (damageByEntityEvent.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) damageByEntityEvent.getDamager();

                // Search for the match where the catcher has been hit by the runner:
                for (Match match : Main.getGameManager().getMatches()) {
                    if (match.getCatcherPlayer() == player && match.getRunnerPlayer() == arrow.getShooter() &&
                            match.getCurrentPhase() == Match.Phase.SHOOTING) {
                        // The current phase should be SHOOTING, because in other phases the runner shouldn't be able
                        // to shoot.

                        match.runnerHitCatcherWithArrow();
                        arrow.remove();
                        event.setCancelled(true);
                        break;
                    }
                }

            } else if (damageByEntityEvent.getDamager() instanceof Player) {
                // PvP is disabled in every case
                event.setCancelled(true);
            }

        } else if (!Main.getGameManager().isPlaying(player)) {
            // Other damages (not EntityDamageByEntity) are only cancelled in the lobby.
            event.setCancelled(true);
        }

        if (!event.isCancelled() && player.getHealth() - event.getDamage() <= 0) {
            // the player would die
            event.setCancelled(true);

            // Search for the match where the player died:
            for (Match match : Main.getGameManager().getMatches()) {
                if (match.getCatcherPlayer() == player) {
                    match.catcherDied();
                    break;
                } else if (match.getRunnerPlayer() == player) {
                    match.runnerDied();
                    break;
                }
                // TODO clear player
            }
        }
    }

}
