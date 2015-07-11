package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

/**
 * Used to notify a match that the arrow has hit a block.
 */
public class ArrowListener implements Listener {

    /**
     * Executed when a projectile hits a block.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return; // we only want to listen to arrows
        Arrow arrow = (Arrow) event.getEntity();

        if (!(arrow.getShooter() instanceof Player)) return; // we only want to listen to arrows shooted by players
        Player player = (Player) arrow.getShooter();

        // Use of a BlockIterator to check where the arrow has landed:
        BlockIterator blockIterator = new BlockIterator(arrow.getWorld(), arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 4);
        Block block = null;
        while(blockIterator.hasNext()) {
            block = blockIterator.next();
            if (block.getType().isSolid()) break; // non-solid blocks are not allowed
        }

        // Assuming the loop has found a block, we are searching in the list of matches for a game where the runner has
        // shooted the arrow and notifying the match afterwords:
        for (Match match : Main.getGameManager().getMatches()) {
            if (match.getRunnerPlayer() == player && match.getCurrentPhase() == Match.Phase.SHOOTING) {
                // runner has shooted the array in the phase SHOOTING
                arrow.remove();
                match.runnerHitBlock(block);
                break;
            }
        }
    }

}
