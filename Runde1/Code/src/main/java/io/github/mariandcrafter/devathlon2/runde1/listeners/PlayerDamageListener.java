package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;

            if (damageByEntityEvent.getDamager() instanceof Arrow) {

                if(Main.getGameManager().playerHitByArrow((Arrow) damageByEntityEvent.getDamager(), player)) {
                    event.setCancelled(true);
                }

            } else if (damageByEntityEvent.getDamager() instanceof Player) {
                event.setCancelled(true);
            }

        } else if (!Main.getGameManager().isPlaying(player)) {
            event.setCancelled(true);
        }
    }

}
