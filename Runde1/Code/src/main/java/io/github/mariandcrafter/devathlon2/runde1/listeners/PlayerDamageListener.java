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
 * This class also checks whether the runner hits the catcher with the arrow, this is not possible.
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

        if (!Main.getGameManager().isPlaying(player)) {
            // Players in the lobby shouldn't get damage.
            event.setCancelled(true);

        } else if (event instanceof EntityDamageByEntityEvent) {
            // If the player is in a game, he can get damage, but not from the other player and not if the runner shootes
            // the catcher with an arrow.

            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;

            if (damageByEntityEvent.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) damageByEntityEvent.getDamager();

                // Search for the match where the catcher has been hit by the runner:
                for (Match match : Main.getGameManager().getMatches()) {
                    if (match.getCatcherPlayer() == player && match.getRunnerPlayer() == arrow.getShooter()) {
                        // runner shooted catcher, cancel:
                        event.setCancelled(true);
                    }
                }

            } else if (damageByEntityEvent.getDamager() instanceof Player) {
                // PvP is always disabled
                event.setCancelled(true);
            }

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
            }
        }
    }

}
