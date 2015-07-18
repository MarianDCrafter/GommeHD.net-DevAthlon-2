package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PressurePlateListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null ||
                !Main.getGameManager().isPlaying(event.getPlayer())) return;

        Player player = event.getPlayer();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null || match.getPhase() != Match.Phase.RUNNING) return;

        Long currentPressurePlateTime = match.getPressurePlateStartTimes().get(player.getUniqueId());
        if (currentPressurePlateTime != null && System.currentTimeMillis() - currentPressurePlateTime < 60000) {
            MessageUtils.error(player, "Du kannst erst wieder in " + ((60000 - (System.currentTimeMillis() - currentPressurePlateTime)) / 1000) + " Sekunden eine Druckplatte verwenden.");
            return;
        }

        if (event.getClickedBlock().getType() == Material.GOLD_PLATE) {
            speed(player);
        } else if (event.getClickedBlock().getType() == Material.WOOD_PLATE) {
            regeneration(match, player);
        } else if (event.getClickedBlock().getType() == Material.STONE_PLATE) {
            teleport(match, player);
        } else if (event.getClickedBlock().getType() == Material.IRON_PLATE) {
            grenade(player);
        }

        match.getPressurePlateStartTimes().put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void speed(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
        MessageUtils.success(player, "Du hast jetzt für 10 Sekunden Speed.");
    }

    private void regeneration(Match match, Player player) {
        boolean regeneration = match.getRunnerPlayer() == player && Main.getRandom().nextBoolean();
        if (regeneration) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
            MessageUtils.success(player, "Du hast jetzt für 5 Sekunden Regeneration.");
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 2));
            MessageUtils.success(player, "Du hast jetzt für 15 Sekunden Übelkeit.");
        }
    }

    private void teleport(Match match, Player player) {
        List<Location> teleportationPoints = match.getGameMap().getTeleportationPoints();
        player.teleport(teleportationPoints.get(Main.getRandom().nextInt(teleportationPoints.size())));
        MessageUtils.success(player, "Du wurdest an einen zufälligen Ort teleportiert.");
    }

    private void grenade(Player player) {
        ItemStack itemStack = new ItemStack(Material.SNOW_BALL);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Blindheits-Granate");
        itemStack.setItemMeta(meta);

        player.getInventory().addItem(itemStack);
        player.updateInventory();

        MessageUtils.success(player, "Du hast eine Blindheits-Granate bekommen. Triff damit den Gegner.");
    }

}
