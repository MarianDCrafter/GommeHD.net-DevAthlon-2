package io.github.mariandcrafter.devathlon2.runde3.listeners;

import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class DisableMobSpawnListener implements Listener {

    @EventHandler
    public void entitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Villager) && !(event.getEntity() instanceof Silverfish)) {
            event.setCancelled(true);
        }
    }

}
