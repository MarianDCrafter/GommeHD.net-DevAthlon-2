package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import io.github.mariandcrafter.devathlon2.runde2.utils.ItemUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Used to check whether a player is staying on a pressure plate to give him effects or other things.
 */
public class PressurePlateListener implements Listener {

    /**
     * Executed when a player interacts.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // The action of a pressure plate is PHYSICAL:
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null ||
                !Main.getGameManager().isPlaying(event.getPlayer())) return;

        Player player = event.getPlayer();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null || match.getPhase() != Match.Phase.RUNNING) return;

        // Calculate whether he is able to use again a pressure plate (every 60 seconds):
        Long currentPressurePlateTime = match.getPressurePlateUseTimes().get(player.getUniqueId());

        if (currentPressurePlateTime != null && System.currentTimeMillis() - currentPressurePlateTime < 60000) {
            // The formular for the rest of the waiting time is (60000 - (currentTime - currentPressurePlateTime)) / 1000:
            MessageUtils.error("Du kannst erst wieder in " + ((60000 - (System.currentTimeMillis() - currentPressurePlateTime)) / 1000) + " Sekunden eine Druckplatte verwenden.", player);
            return;
        }

        // Check the type of pressure plate:
        switch (event.getClickedBlock().getType()) {
            case GOLD_PLATE:
                speed(player);
                break;
            case WOOD_PLATE:
                regeneration(match, player);
                break;
            case STONE_PLATE:
                teleport(match, player);
                break;
            case IRON_PLATE:
                grenade(player);
                break;
        }

        // Play sound and effect:
        PlayerUtils.playSound(Sound.NOTE_STICKS, 10, 1, player);
        PlayerUtils.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null, player);

        // Put the current time into the map to prevent the player from using again a pressure plate within 60 seconds:
        match.getPressurePlateUseTimes().put(player.getUniqueId(), System.currentTimeMillis());
    }

    /**
     * Gives the given player speed.
     *
     * @param player the player to give speed.
     */
    private void speed(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
        MessageUtils.success("Du hast jetzt für 10 Sekunden Speed.", player);
    }

    /**
     * Gives the given player regeneration. If the player is a catcher or with a probability of 50% the player gets confusion.
     *
     * @param match  the current match of the player
     * @param player the player to give regeneration
     */
    private void regeneration(Match match, Player player) {
        if (match.getRunnerPlayer() == player && Main.getRandom().nextBoolean()) {
            // Player is runner and he lucky:
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
            MessageUtils.success("Du hast jetzt für 5 Sekunden Regeneration.", player);
        } else {
            // Player is catcher or he is unlucky:
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 2));
            MessageUtils.success("Du hast jetzt für 15 Sekunden Übelkeit.", player);
        }
    }

    /**
     * Teleports the given player to a random teleportation point of the map.
     *
     * @param match  the current match of the player
     * @param player the player to teleport
     */
    private void teleport(Match match, Player player) {
        // Random teleportation point:
        List<Location> teleportationPoints = match.getGameMap().getTeleportationPoints();
        player.teleport(teleportationPoints.get(Main.getRandom().nextInt(teleportationPoints.size())));

        MessageUtils.success("Du wurdest an einen zufälligen Ort teleportiert.", player);
    }

    /**
     * Gives the given player a blindess grenade.
     *
     * @param player the player to give a blindness grenade
     */
    private void grenade(Player player) {
        player.getInventory().addItem(ItemUtils.createItemStackWithName(Material.SNOW_BALL, ChatColor.GOLD + "Blindheits-Granate"));
        player.updateInventory();

        MessageUtils.success("Du hast eine Blindheits-Granate bekommen. Triff damit den Gegner.", player);
    }

}
