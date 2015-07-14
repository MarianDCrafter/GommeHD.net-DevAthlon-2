package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Used to disable that players can pick up or drop items.
 * Nether stars are allowed when the player is the catcher, because he has to put it into the hopper. Also picking up
 * a nether star is allowed as the catcher, because maybe he drops it at a wrong place.
 */
public class ItemPickupDropListener implements Listener {

    /**
     * Called when a player picks up an item.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Match match = Main.getGameManager().getMatch(event.getPlayer());

        // only allow nether stars and if the player is a catcher
        if (event.getItem().getItemStack().getType() != Material.NETHER_STAR ||
                match.getCatcherPlayer() != event.getPlayer())
            event.setCancelled(true);
    }

    /**
     * Called when a player drops an item.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onItemDropped(PlayerDropItemEvent event) {
        Match match = Main.getGameManager().getMatch(event.getPlayer());

        // only allow nether stars and if the player is a catcher
        if (event.getItemDrop().getItemStack().getType() != Material.NETHER_STAR ||
                match.getCatcherPlayer() != event.getPlayer())
            event.setCancelled(true);
    }

}
