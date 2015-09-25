package io.github.mariandcrafter.devathlon2.runde3.commands;

import io.github.mariandcrafter.bukkitpluginapi.commands.Command;
import io.github.mariandcrafter.bukkitpluginapi.commands.CommandSenderType;
import org.bukkit.entity.Player;

import java.util.List;

public class OfferCommand {

    @Command(
            command = "acceptoffer",
            only = CommandSenderType.PLAYER,
            minArgumentLength = 1
    )
    public void offer(Player player, List<String> arguments) {
        player.sendMessage("Du hast Angebot " + arguments.get(0) + " angenommen.");
    }

}
