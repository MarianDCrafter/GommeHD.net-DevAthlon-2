package io.github.mariandcrafter.devathlon2.runde2.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class RegenerationListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onRegeneration(EntityRegainHealthEvent event) {
        if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC &&
                event.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
            event.setCancelled(true);
        }
    }

}
