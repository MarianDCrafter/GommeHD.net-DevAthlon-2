package io.github.mariandcrafter.devathlon2.runde2.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Used to prevent players from dropping items.
 */
public class ItemDropListener implements Listener {

    /**
     * Called when a player drops an item. Event will be cancelled.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

}
