package io.github.mariandcrafter.devathlon2.runde1.listeners;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.game.Match;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemPickupListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Match match = Main.getGameManager().getMatch(event.getPlayer());

        if (event.getItem().getItemStack().getType() != Material.NETHER_STAR ||
                match.getCatcherPlayer() != event.getPlayer())
            event.setCancelled(true);
    }

}
