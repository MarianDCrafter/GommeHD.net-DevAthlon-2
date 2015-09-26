package io.github.mariandcrafter.devathlon2.runde3.listeners;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            System.out.println(-1);
            if (!Main.getGameManager().getPlaying().contains(player.getUniqueId())) {
                System.out.println(-2);
                event.setCancelled(true);
            } else {
                System.out.println(0);
                if (event instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
                    if (damageByEntityEvent.getDamager() instanceof Player) {
                        event.setCancelled(true);
                        return;
                    }
                }
                if (player.getHealth() - event.getDamage() <= 0) {
                    event.setCancelled(true);
                    System.out.println(1);
                    Main.getGameManager().removePlayerFromGame(player);
                    System.out.println(100);
                    Main.getGameManager().joinLobby(player);
                    System.out.println(10000);
                }
            }
        } else {
            event.setCancelled(true);
        }
    }

}
