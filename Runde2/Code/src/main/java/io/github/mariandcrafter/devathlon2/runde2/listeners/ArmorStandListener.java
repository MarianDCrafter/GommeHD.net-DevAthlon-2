package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorStandListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (event.getArmorStandItem() != null && event.getArmorStandItem().getType() != Material.AIR &&
                (event.getPlayerItem() == null || event.getPlayerItem().getType() == Material.AIR)) {
            // Only executing if the player takes something from the armor stand, not if he puts something into it.
            // The documentation says that the item stacks are null if not used, but I found out that the types are AIR.

            Player player = event.getPlayer();
            Match match = Main.getGameManager().getMatch(player);

            if (match != null && match.getRunnerPlayer() == player && match.getPhase() == Match.Phase.RUNNING) {
                runnerTakesItem(player, match, event.getArmorStandItem(), event.getRightClicked());
            }
        }
        event.setCancelled(true);
    }

    private void runnerTakesItem(Player player, Match match, ItemStack itemStack, ArmorStand armorStand) {
        if (Main.getRandom().nextInt(100) < 75) {
            // in 75% of all cases the player should get the item
            Material material = itemStack.getType();
            switch (material) {
                case IRON_HELMET:
                    player.getInventory().setHelmet(new ItemStack(Material.GLASS));
                    MessageUtils.success(player, "Du hast jetzt den Astronautenhelm!");
                    MessageUtils.info(match.getCatcherPlayer(), "Der Runner hat jetzt den Astronautenhelm!");
                    break;
                case IRON_CHESTPLATE:
                    player.getInventory().setChestplate(itemStack);
                    MessageUtils.success(player, "Du hast jetzt die Astronautenbrustplatte!");
                    MessageUtils.info(match.getCatcherPlayer(), "Der Runner hat jetzt die Astronautenbrustplatte!");
                    break;
                case IRON_LEGGINGS:
                    player.getInventory().setLeggings(itemStack);
                    MessageUtils.success(player, "Du hast jetzt die Astronautenhose!");
                    MessageUtils.info(match.getCatcherPlayer(), "Der Runner hat jetzt die Astronautenhose!");
                    break;
                case IRON_BOOTS:
                    player.getInventory().setBoots(itemStack);
                    MessageUtils.success(player, "Du hast jetzt die Astronautenschuhe!");
                    MessageUtils.info(match.getCatcherPlayer(), "Der Runner hat jetzt die Astronautenschuhe!");
                    break;
            }
            match.getGameMap().removeArmorStand(armorStand);

            if (match.runnerHasCompleteArmor()) {
                MessageUtils.success(player, "Du hast jetzt die komplette Astronautenrüstung! Begib dich schnell zur Rettungskapsel!");
                MessageUtils.info(match.getCatcherPlayer(), "Der Runner hat jetzt die komplette Astronautenrüstung!");
                match.giveRunnerRescueCapsuleCompass();
            }
            match.updateRunnerCompass();

        } else {
            // unlucky, the armor stand teleports itself to another location
            match.getGameMap().teleportArmorStand(armorStand, itemStack);
            match.updateRunnerCompass();
            MessageUtils.error(player, "Leider hat sich der Armor Stand wegteleportiert.");
        }
    }

}
