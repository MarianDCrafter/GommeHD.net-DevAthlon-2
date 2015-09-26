package io.github.mariandcrafter.devathlon2.runde3.listeners;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.game.Game;
import io.github.mariandcrafter.devathlon2.runde3.game.swordplay.SwordplayGame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.UUID;

public class DamageListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!Main.getGameManager().getPlaying().contains(player.getUniqueId())) {
                event.setCancelled(true);
            } else {
                SwordplayGame swordplayGame = null;
                Player swordplayWinner = null;

                if (event instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
                    if (damageByEntityEvent.getDamager() instanceof Player) {
                        Player damager = (Player) damageByEntityEvent.getDamager();
                        Map<UUID, Game> games = Main.getGameManager().getGames();
                        if (games.containsKey(player.getUniqueId()) == games.containsKey(damager.getUniqueId()) &&
                                games.get(player.getUniqueId()) instanceof SwordplayGame) {
                            swordplayGame = (SwordplayGame) games.get(player.getUniqueId());
                            swordplayWinner = damager;
                        } else {
                            event.setCancelled(true);
                        }
                    }
                }

                if (player.getHealth() - event.getDamage() <= 0) {
                    event.setCancelled(true);
                    if (swordplayGame != null) {
                        swordplayGame.endGameWithWinner(swordplayWinner, player);
                    } else {
                        Main.getGameManager().removePlayerFromGame(player);
                        Main.getGameManager().joinLobby(player);
                    }
                }
            }
        } else if (event.getEntity() instanceof Villager) {
            event.setCancelled(true);
        }
    }

}
