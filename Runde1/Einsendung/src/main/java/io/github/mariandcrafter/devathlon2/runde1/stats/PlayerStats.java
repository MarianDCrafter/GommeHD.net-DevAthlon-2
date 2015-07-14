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
import java.util.UUID;

/**
 * Selects the stats of a player from the database.
 */
public class PlayerStats {

    private Player player;
    private UUID uuid;

    private int place = 0;
    private int played, won, lost;
    private int runsStarted, runsFinished, runsNotFinished;
    private int arrowsShooted, arrowsEnemyGot, arrowsEnemyGotNot;
    private int arrowsEnemyShooted, arrowsGot, arrowsGotNot;

    /**
     * @param player the player who wants to view the stats
     * @param uuid the uuid of the player with the stats
     */
    public PlayerStats(Player player, UUID uuid) {
        this.player = player;
        this.uuid = uuid;
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
     * Loads the stats from the database.
     */
    private void load() {
        String uuidString = UUIDUtils.stringFromUUID(uuid);

        try {
            Statement statement = Main.getPluginDatabase().createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS played FROM participations WHERE player='" + uuidString + "'");
            if (resultSet.next()) played = resultSet.getInt("played");

            resultSet = statement.executeQuery("SELECT COUNT(*) AS won FROM participations WHERE won=1 AND player='" + uuidString + "'");
            if (resultSet.next()) won = resultSet.getInt("won");

            resultSet = statement.executeQuery("SELECT COUNT(*) AS lost FROM participations WHERE won=0 AND player='" + uuidString + "'");
            if (resultSet.next()) lost = resultSet.getInt("lost");

            resultSet = statement.executeQuery("SELECT SUM(startedRuns) AS runsStarted FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) runsStarted = resultSet.getInt("runsStarted");

            resultSet = statement.executeQuery("SELECT SUM(finishedRuns) AS runsFinished FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) runsFinished = resultSet.getInt("runsFinished");

            resultSet = statement.executeQuery("SELECT (SUM(startedRuns) - SUM(finishedRuns)) AS runsNotFinished FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) runsNotFinished = resultSet.getInt("runsNotFinished");

            resultSet = statement.executeQuery("SELECT SUM(shootedArrows) AS arrowsShooted FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) arrowsShooted = resultSet.getInt("arrowsShooted");

            resultSet = statement.executeQuery("SELECT SUM(enemyGotArrows) AS arrowsEnemyGot FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) arrowsEnemyGot = resultSet.getInt("arrowsEnemyGot");

            resultSet = statement.executeQuery("SELECT (SUM(shootedArrows) - SUM(enemyGotArrows)) AS arrowsEnemyGotNot FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) arrowsEnemyGotNot = resultSet.getInt("arrowsEnemyGotNot");

            resultSet = statement.executeQuery("SELECT SUM(enemyShootedArrows) AS arrowsEnemyShooted FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) arrowsEnemyShooted = resultSet.getInt("arrowsEnemyShooted");

            resultSet = statement.executeQuery("SELECT SUM(gotArrows) AS arrowsGot FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) arrowsGot = resultSet.getInt("arrowsGot");

            resultSet = statement.executeQuery("SELECT (SUM(enemyShootedArrows) - SUM(gotArrows)) AS arrowsGotNot FROM participations GROUP BY player HAVING player='" + uuidString + "'");
            if (resultSet.next()) arrowsGotNot = resultSet.getInt("arrowsGotNot");

            resultSet = statement.executeQuery("SELECT player FROM participations WHERE won=1 GROUP BY player ORDER BY COUNT(*) DESC");
            while(resultSet.next()) {
                place++;
                if (UUIDUtils.stringToUUID(resultSet.getString("player")).equals(uuid))
                    break;
            }

            statement.close();

            send();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the stats to the player.
     */
    private void send() {
        player.sendMessage(ChatColor.GRAY + "[]--------[] " + ChatColor.YELLOW + "Stats von " + ChatColor.GOLD + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.GRAY + " []--------[]");
        player.sendMessage(ChatColor.YELLOW + "- Rang: " + ChatColor.GOLD + "#" + place);
        player.sendMessage(ChatColor.YELLOW + "- Gespielte Spiele: " + ChatColor.GOLD + played + ChatColor.YELLOW + " (" + ChatColor.GOLD + won + ChatColor.YELLOW + " gewonnen, " + ChatColor.GOLD + lost + ChatColor.YELLOW + " verloren)");
        player.sendMessage(ChatColor.YELLOW + "- Begonnene Läufe: " + ChatColor.GOLD + runsStarted + ChatColor.YELLOW + " (" + ChatColor.GOLD + runsFinished + ChatColor.YELLOW + " beendet, " + ChatColor.GOLD + runsNotFinished + ChatColor.YELLOW + " verloren)");
        player.sendMessage(ChatColor.YELLOW + "- Geschossene Pfeile: " + ChatColor.GOLD + arrowsShooted + ChatColor.YELLOW + " (" + ChatColor.GOLD + arrowsEnemyGot + ChatColor.YELLOW + " hat der Gegner in den Hopper getan, " + ChatColor.GOLD + arrowsEnemyGotNot + ChatColor.YELLOW + " nicht)");
        player.sendMessage(ChatColor.YELLOW + "- Vom Gegener geschossene Pfeile: " + ChatColor.GOLD + arrowsEnemyShooted + ChatColor.YELLOW + " (" + ChatColor.GOLD + arrowsGot + ChatColor.YELLOW + " in den Hopper getan, " + ChatColor.GOLD + arrowsGotNot + ChatColor.YELLOW + " nicht)");
        player.sendMessage(ChatColor.GRAY + "[]---------------------------------[]");
    }

}
