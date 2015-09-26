package io.github.mariandcrafter.devathlon2.runde3.listeners;

import io.github.mariandcrafter.bukkitpluginapi.utils.BukkitUtils;
import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.game.Game;
import io.github.mariandcrafter.devathlon2.runde3.game.Gamemode;
import io.github.mariandcrafter.devathlon2.runde3.game.swordplay.Swordplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if ((event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {

            if (Main.getGameManager().getGames().containsKey(event.getPlayer().getUniqueId())) {
                Game game = Main.getGameManager().getGames().get(event.getPlayer().getUniqueId());
                Gamemode gamemode = game.getGamemode();
                if (gamemode.getArea() != null && !gamemode.getArea().containsBlockLocation(event.getTo())) {
                    game.playerLeftArea();
                }
            } else {
                Swordplay swordplay = Main.getConfiguration().getSwordplay();
                Player player1 = null, player2 = null;
                for (Player player : BukkitUtils.getOnlinePlayers()) {
                    if (player.getLocation().getBlock().getLocation().equals(swordplay.getPlate1()))
                        player1 = player;
                    else if (player.getLocation().getBlock().getLocation().equals(swordplay.getPlate2()))
                        player2 = player;
                }
                if (player1 != null && player2 != null) {
                    swordplay.tryStartAGameWith(player1, player2);
                }
            }
        }
    }

}
