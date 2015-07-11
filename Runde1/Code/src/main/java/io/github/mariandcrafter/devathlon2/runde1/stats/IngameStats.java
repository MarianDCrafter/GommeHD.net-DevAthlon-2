package io.github.mariandcrafter.devathlon2.runde1.stats;

import io.github.mariandcrafter.devathlon2.runde1.utils.UUIDUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class IngameStats {

    private boolean won = false;
    private int startedRuns = 0, finishedRuns = 0;
    private int shootedArrows = 0, enemyGotArrows = 0;
    private int enemyShootedArrows = 0, gotArrows = 0;

    public void won() {
        won = true;
    }

    public void incrementStartedRuns() {
        startedRuns++;
    }

    public void incrementFinishedRuns() {
        finishedRuns++;
    }

    public void incrementShootedArrows() {
        shootedArrows++;
    }

    public void incrementEnemyGotArrows() {
        enemyGotArrows++;
    }

    public void incrementEnemyShootedArrows() {
        enemyShootedArrows++;
    }

    public void incrementGotArrows() {
        gotArrows++;
    }

    public int getStartedRuns() {
        return startedRuns;
    }

    public int getFinishedRuns() {
        return finishedRuns;
    }

    public int getShootedArrows() {
        return shootedArrows;
    }

    public int getEnemyGotArrows() {
        return enemyGotArrows;
    }

    public int getEnemyShootedArrows() {
        return enemyShootedArrows;
    }

    public int getGotArrows() {
        return gotArrows;
    }

    public void insertIntoDatabase(Statement statement, UUID uuid, int matchID) throws SQLException {
        String sql = "INSERT INTO participations (matchID, player, won, startedRuns, finishedRuns, shootedArrows, " +
                "enemyGotArrows, enemyShootedArrows, gotArrows) VALUES (" + matchID +  ", '" + UUIDUtils.stringFromUUID(uuid) + "', " +
                (won ? 1 : 0) + ", " + startedRuns + ", " + finishedRuns + ", " + shootedArrows + ", " + enemyGotArrows + ", " +
                enemyShootedArrows + ", " + gotArrows + ")";
        System.out.println(sql);
        statement.executeUpdate(sql);
    }

}
