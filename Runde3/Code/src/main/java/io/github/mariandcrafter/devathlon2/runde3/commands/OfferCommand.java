package io.github.mariandcrafter.devathlon2.runde3.commands;

import io.github.mariandcrafter.bukkitpluginapi.commands.Command;
import io.github.mariandcrafter.bukkitpluginapi.commands.CommandSenderType;
import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.game.Offer;
import org.bukkit.entity.Player;

import java.util.List;

public class OfferCommand {

    @SuppressWarnings("unused")
    @Command(
            command = "acceptoffer",
            only = CommandSenderType.PLAYER,
            minArgumentLength = 1
    )
    public void offer(Player player, List<String> arguments) {
        if (!Main.getGameManager().getPlaying().contains(player.getUniqueId())) {
            player.sendMessage("§cDu spielst nicht mit.");
        } else if (Main.getGameManager().getGames().containsKey(player.getUniqueId())) {
            player.sendMessage("§cDu spielst bereits ein Minispiel.");
        } else {
            Offer.OFFERS.get(Integer.parseInt(arguments.get(0))).acceptOffer(player);
        }
    }

}
