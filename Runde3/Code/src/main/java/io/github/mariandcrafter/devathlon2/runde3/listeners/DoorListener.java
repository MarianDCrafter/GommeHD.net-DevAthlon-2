package io.github.mariandcrafter.devathlon2.runde3.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoorListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock().getType().toString().contains("DOOR"))
            event.setCancelled(true);
    }

}
