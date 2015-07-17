package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        event.setCancelled(true);
        for (Match match : Main.getGameManager().getMatches()) {
            if (match.getRunnerPlayer() == player) {
                if (player.getHealth() - event.getDamage() <= 0) {
                    match.catcherKilledRunner();
                } else {
                    event.setCancelled(false);
                }
            }
        }
    }

}
