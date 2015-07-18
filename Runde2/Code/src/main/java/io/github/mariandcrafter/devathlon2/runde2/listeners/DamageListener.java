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

        event.setCancelled(true);

        Player player = (Player) event.getEntity();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null || match.getRunnerPlayer() != player || match.getPhase() != Match.Phase.RUNNING) return;

        if (player.getHealth() - event.getDamage() <= 0)
            match.catcherKilledRunner();
        else
            event.setCancelled(false);
    }

}
