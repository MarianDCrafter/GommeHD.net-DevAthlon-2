package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

/**
 * Used to check when a hopper has picked up a nether star.
 */
public class HopperPickupListener implements Listener {

    /**
     * Executed when an inventory picks up an item.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (event.getItem().getItemStack().getType() != Material.NETHER_STAR ||
                !(event.getInventory().getHolder() instanceof Hopper)) return;
        // we only want to listen to hoppers that picked up a nether star

        Location location = ((Hopper) event.getInventory().getHolder()).getLocation();

        // Search for the match that contains the hopper that picked up an item, clear that inventory so that the
        // nether star will disappear and notify the match about the event:
        for (Match match : Main.getGameManager().getMatches()) {
            if (match.getGameMap().getHoppers().contains(location)) {
                event.getInventory().clear();
                match.ballInsertedIntoHopper();
                break;
            }
        }
    }

}
