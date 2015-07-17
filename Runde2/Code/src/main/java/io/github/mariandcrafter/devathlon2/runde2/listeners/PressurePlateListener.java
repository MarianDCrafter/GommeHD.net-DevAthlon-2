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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PressurePlateListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null ||
                !Main.getGameManager().isPlaying(event.getPlayer())) return;

        Player player = event.getPlayer();
        Match match = Main.getGameManager().getMatch(player);

        Long currentPressurePlateTime = match.getPressurePlateStartTimes().get(player.getUniqueId());
        if (currentPressurePlateTime != null && System.currentTimeMillis() - currentPressurePlateTime < 60000) {
            MessageUtils.error(player, "Du kannst erst wieder in " + ((60000 - (System.currentTimeMillis() - currentPressurePlateTime)) / 1000) + " Sekunden eine Druckplatte verwenden.");
            return;
        }

        if (event.getClickedBlock().getType() == Material.GOLD_PLATE) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
            MessageUtils.success(player, "Du hast jetzt für 10 Sekunden Speed.");

        } else if (event.getClickedBlock().getType() == Material.WOOD_PLATE) {
            boolean regeneration = match.getRunnerPlayer() == player && Main.getRandom().nextBoolean();
            if (regeneration) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                MessageUtils.success(player, "Du hast jetzt für 5 Sekunden Regeneration.");
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 30, 2));
                MessageUtils.success(player, "Du hast jetzt für 15 Sekunden Übelkeit.");
            }
        }

        match.getPressurePlateStartTimes().put(player.getUniqueId(), System.currentTimeMillis());
    }

}
