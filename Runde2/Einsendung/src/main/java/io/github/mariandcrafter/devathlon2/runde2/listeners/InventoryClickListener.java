package io.github.mariandcrafter.devathlon2.runde2.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Used to cancel inventory interactions.
 */
public class InventoryClickListener implements Listener {

    /**
     * Called when someone clicks in an inventory. Event will be cancelled.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

}
