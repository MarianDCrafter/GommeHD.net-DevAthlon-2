package io.github.mariandcrafter.devathlon2.runde2.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

/**
 * Used to disable every regeneration, except from regeneration of potion effects.
 */
public class RegenerationListener implements Listener {

    /**
     * Executed when a player regenerates. Event will be cancelled if the reason is not a potion effect.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onRegeneration(EntityRegainHealthEvent event) {
        if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC &&
                event.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
            event.setCancelled(true);
        }
    }

}
