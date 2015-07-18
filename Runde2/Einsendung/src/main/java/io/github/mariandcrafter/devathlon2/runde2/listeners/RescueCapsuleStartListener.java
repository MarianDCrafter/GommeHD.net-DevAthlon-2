package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class RescueCapsuleStartListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                (event.getClickedBlock().getType() != Material.STONE_BUTTON && event.getClickedBlock().getType() != Material.WOOD_BUTTON))
            return;

        Player player = event.getPlayer();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null || match.getPhase() != Match.Phase.RUNNING ||
                !match.getGameMap().getRescueCapsule().getRescueButtonLocation().equals(event.getClickedBlock().getLocation()))
            return;

        if (match.getRunnerPlayer() != player) {
            MessageUtils.error("Nur der Runner kann die Rettungskapsel verwenden!", player);
            return;
        }

        if (match.runnerHasCompleteArmor())
            match.runnerUsedRescueCapsuleWithArmor();
        else
            match.runnerUsedRescueCapsuleWithoutFullArmor();
    }

}
