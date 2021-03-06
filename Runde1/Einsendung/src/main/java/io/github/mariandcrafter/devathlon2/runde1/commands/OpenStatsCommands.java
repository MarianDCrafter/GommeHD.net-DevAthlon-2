package io.github.mariandcrafter.devathlon2.runde1.commands;

import io.github.mariandcrafter.devathlon2.runde1.stats.PlayerStats;
import io.github.mariandcrafter.devathlon2.runde1.stats.RankingStats;
import io.github.mariandcrafter.devathlon2.runde1.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Command to open stats.
 */
public class OpenStatsCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl verwenden.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equals("stats")) {

            if (args.length == 0) {
                // send him his own stats
                stats(player, player.getUniqueId());

            } else {
                //noinspection deprecation
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (!offlinePlayer.hasPlayedBefore()) {
                    player.sendMessage(MessageUtils.message(ChatColor.RED + "Dieser Spieler war noch nie auf dem Server."));
                    return true;
                }

                // send him the stats of the specified player
                stats(player, offlinePlayer.getUniqueId());
            }
            return true;

        } else if(command.getName().equals("ranking")) {

            new RankingStats(player).show();
            return true;

        } else {
            return false;
        }
    }

    private void stats(Player player, UUID uuid) {
        new PlayerStats(player, uuid).show();
    }

}
