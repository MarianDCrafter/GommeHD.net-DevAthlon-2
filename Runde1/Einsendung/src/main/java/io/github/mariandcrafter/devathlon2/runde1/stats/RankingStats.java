package io.github.mariandcrafter.devathlon2.runde1.stats;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde1.utils.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Selects the top 5 players from the database.
 */
public class RankingStats {

    private Player player;

    /**
     * @param player the player who wants to view the stats
     */
    public RankingStats(Player player) {
        this.player = player;
    }

    /**
     * Loads the stats from the database in an external thread.
     */
    public void show() {
        player.sendMessage(MessageUtils.message(ChatColor.GOLD + "Statistiken werden geladen..."));

        new Thread(new Runnable() {
            @Override
            public void run() {
                load();
            }
        }).start();
    }

    /**
     * Loads the stats from the database and sends them to the player.
     */
    private void load() {
        try{
            Statement statement = Main.getPluginDatabase().createStatement();


            ResultSet resultSet = statement.executeQuery("SELECT player, COUNT(*) FROM participations WHERE won=1 GROUP BY player ORDER BY COUNT(*) DESC LIMIT 5");

            player.sendMessage(ChatColor.GRAY + "[]--------[] " + ChatColor.YELLOW + "Top Ranking" + ChatColor.GRAY + " []--------[]");
            int number = 0;
            while(resultSet.next()) {
                number++;
                String playerName = Bukkit.getOfflinePlayer(UUIDUtils.stringToUUID(resultSet.getString("player"))).getName();
                player.sendMessage(ChatColor.YELLOW + "#" + number + " " + ChatColor.GOLD + playerName + ChatColor.YELLOW + " (" + ChatColor.GOLD + resultSet.getInt("COUNT(*)") + ChatColor.YELLOW + " gewonnene Spiele)");
            }

            if (number == 0) {
                player.sendMessage(ChatColor.YELLOW + "Keine gespielten Spiele");
            }

            player.sendMessage(ChatColor.GRAY + "[]---------------------------------[]");

            statement.close();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
