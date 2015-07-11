package io.github.mariandcrafter.devathlon2.runde1.stats;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.utils.UUIDUtils;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class PlayerStats {

    private int played, won, lost;
    private int runsStarted, runsFinished, runsNotFinished;

    public PlayerStats(Player player, UUID uuid) {
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

            print();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void print() {
        System.out.println(played + " " + won + " " + lost);
        System.out.println(runsStarted + " " + runsFinished + " " + runsNotFinished);
    }

}
