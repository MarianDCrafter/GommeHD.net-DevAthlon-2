package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

public class HopperPickupListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (event.getItem().getItemStack().getType() != Material.NETHER_STAR ||
                !(event.getInventory().getHolder() instanceof Hopper)) return;

        Location location = ((Hopper) event.getInventory().getHolder()).getLocation();

        for (Match match : Main.getGameManager().getMatches()) {
            if (match.getGameMap().getHoppers().contains(location)) {
                event.getInventory().clear();
                match.ballInsertedIntoHopper();
            }
        }
    }

}
