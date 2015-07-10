package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Used to check whether a catcher has clicked on the 'ball'.
 */
public class BallClickListener implements Listener {

    /**
     * Executed when a player interacts.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        // we only want to listen to left/right clicks on a block

        Player player = event.getPlayer();

        // Search for the match where the catcher is the player who clicked on the block and notify the match:
        for (Match match : Main.getGameManager().getMatches()) {
            if (match.getCatcherPlayer() == player) {
                match.catcherClickedBlock(event.getClickedBlock());
                break;
            }
        }
    }

}
