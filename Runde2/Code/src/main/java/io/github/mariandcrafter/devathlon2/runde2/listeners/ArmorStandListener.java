package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Used to get notified when a player takes armor from an armor stand.
 */
public class ArmorStandListener implements Listener {

    /**
     * Called when a player interacts with an armor stand
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (event.getArmorStandItem() != null && event.getArmorStandItem().getType() != Material.AIR &&
                (event.getPlayerItem() == null || event.getPlayerItem().getType() == Material.AIR)) {
            // Only executing if the player takes something from the armor stand, not if he puts something into it.
            // The documentation says that the item stacks are null if not used, but I found out that the types are AIR.

            Player player = event.getPlayer();
            Match match = Main.getGameManager().getMatch(player);

            if (match != null && match.getPhase() == Match.Phase.RUNNING) {
                if (match.getRunnerPlayer() == player)
                    runnerTakesItem(player, match, event.getArmorStandItem(), event.getRightClicked());
                else
                    catcherClickedArmorStand(match, event.getArmorStandItem(), event.getRightClicked());
            }
        }
        event.setCancelled(true);
    }

    /**
     * Called when the runner takes an item from the armor stand.
     * @param player the player who takes an item
     * @param match the current match of the player
     * @param itemStack the item stack the player takes
     * @param armorStand the armor stand
     */
    private void runnerTakesItem(Player player, Match match, ItemStack itemStack, ArmorStand armorStand) {
        if (Main.getRandom().nextInt(100) < 75) {
            // in 75% of all cases the player should get the item

            // Check the type of the item to send messages and set the armor of the player
            Material material = itemStack.getType();
            switch (material) {
                case IRON_HELMET:
                    player.getInventory().setHelmet(new ItemStack(Material.GLASS));
                    MessageUtils.success( "Du hast jetzt den Astronautenhelm!", player);
                    MessageUtils.info("Der Runner hat jetzt den Astronautenhelm!", match.getCatcherPlayer());
                    break;
                case IRON_CHESTPLATE:
                    player.getInventory().setChestplate(itemStack);
                    MessageUtils.success("Du hast jetzt die Astronautenbrustplatte!", player);
                    MessageUtils.info("Der Runner hat jetzt die Astronautenbrustplatte!", match.getCatcherPlayer());
                    break;
                case IRON_LEGGINGS:
                    player.getInventory().setLeggings(itemStack);
                    MessageUtils.success("Du hast jetzt die Astronautenhose!", player);
                    MessageUtils.info("Der Runner hat jetzt die Astronautenhose!", match.getCatcherPlayer());
                    break;
                case IRON_BOOTS:
                    player.getInventory().setBoots(itemStack);
                    MessageUtils.success("Du hast jetzt die Astronautenschuhe!", player);
                    MessageUtils.info("Der Runner hat jetzt die Astronautenschuhe!", match.getCatcherPlayer());
                    break;
            }

            match.getGameMap().removeArmorStand(armorStand);

            if (match.runnerHasCompleteArmor()) {
                // Runner has complete Armor, send messages, play sound and give him the compass which points to the rescue capsule:

                MessageUtils.success("Du hast jetzt die komplette Astronautenrüstung! Begib dich schnell zur Rettungskapsel!", player);
                MessageUtils.info("Der Runner hat jetzt die komplette Astronautenrüstung!", match.getCatcherPlayer());
                PlayerUtils.playSound(Sound.NOTE_BASS, 10, 1, match.getRunnerPlayer(), match.getCatcherPlayer());

                match.giveRunnerRescueCapsuleCompass();

            } else {
                // Runner has not complete armor, play another sound:
                PlayerUtils.playSound(Sound.NOTE_PLING, 10, 1, match.getRunnerPlayer(), match.getCatcherPlayer());
            }

            match.updateRunnerCompass();

        } else {
            // unlucky, the armor stand teleports itself to another location
            match.getGameMap().teleportArmorStand(armorStand, itemStack);
            match.updateRunnerCompass();

            MessageUtils.error("Leider hat sich der Armor Stand wegteleportiert.", player);
        }
    }

    /**
     * Called when the catcher wants to take an item from an armor stand. Teleports the armor stand.
     * @param match the current match of the catcher
     * @param itemStack the item stack the player takes
     * @param armorStand the armor stand
     */
    private void catcherClickedArmorStand(Match match, ItemStack itemStack, ArmorStand armorStand) {
        // Teleport the armor stand:
        match.getGameMap().teleportArmorStand(armorStand, itemStack);
        match.updateRunnerCompass();

        MessageUtils.success("Du hast den Armor Stand wegteleportiert.", match.getCatcherPlayer());
        MessageUtils.success("Der Catcher hat einen Armor Stand woandershin teleportiert.", match.getRunnerPlayer());
    }

}
