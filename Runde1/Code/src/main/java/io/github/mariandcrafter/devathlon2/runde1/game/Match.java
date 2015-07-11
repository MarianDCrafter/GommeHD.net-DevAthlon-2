package io.github.mariandcrafter.devathlon2.runde1.game;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.stats.IngameStats;
import io.github.mariandcrafter.devathlon2.runde1.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde1.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A match between two players.
 */
public class Match {

    /**
     * The possible phases are
     * STARTING (the players can walk through the map),
     * SHOOTING (the runner is shooting) and
     * RUNNING (the runner is running)
     */
    public enum Phase {
        START, SHOOTING, RUNNING
    }

    private final static int TIME_TO_START = 10; // time before the start of a round
    private final static int TIME_PER_ROUND = 120; // time of a round

    private GameMap gameMap;
    private UUID runner;
    private UUID catcher;
    private Map<UUID, IngameStats> ingameStats = new HashMap<UUID, IngameStats>();

    private int time;
    private BukkitTask task;

    private Phase currentPhase;
    private int currentBase = 0;

    private HitBlock hitBlock; // block hit by the runner

    private int roundCount = 0; // current round number

    /**
     * @param gameMap the map for this match
     * @param runner  the uuid of the runner
     * @param catcher the uuid of the catcher
     */
    public Match(GameMap gameMap, UUID runner, UUID catcher) {
        this.gameMap = gameMap;
        this.runner = runner;
        this.catcher = catcher;

        ingameStats.put(runner, new IngameStats());
        ingameStats.put(catcher, new IngameStats());

        // Close all gates of the map and clear the players for safety reasons:
        gameMap.closeAllGates();
        PlayerUtils.clear(getRunnerPlayer());
        PlayerUtils.clear(getCatcherPlayer());
    }

    /**
     * @return the map of this match
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * @return the current phase of this match
     */
    public Phase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * @return the block shooted by the runner
     */
    public HitBlock getHitBlock() {
        return hitBlock;
    }

    /**
     * @return the uuid of the runner
     */
    public UUID getRunner() {
        return runner;
    }

    /**
     * @return the runner
     */
    public Player getRunnerPlayer() {
        return Bukkit.getPlayer(runner);
    }

    /**
     * @return the uuid of the catcher
     */
    public UUID getCatcher() {
        return catcher;
    }

    /**
     * @return the catcher
     */
    public Player getCatcherPlayer() {
        return Bukkit.getPlayer(catcher);
    }

    /**
     * Sends the given message to the two players.
     *
     * @param message the message to send
     */
    private void sendMessage(String message) {
        getRunnerPlayer().sendMessage(message);
        getCatcherPlayer().sendMessage(message);
    }

    /**
     * Sends their roles to the two players.
     */
    private void sendRoles() {
        getRunnerPlayer().sendMessage(MessageUtils.message(ChatColor.GREEN + "Du wirst " + ChatColor.GOLD + "Runner" + ChatColor.GREEN + " sein."));
        getCatcherPlayer().sendMessage(MessageUtils.message(ChatColor.GREEN + "Du wirst " + ChatColor.GOLD + "Catcher" + ChatColor.GREEN + " sein."));
    }

    /**
     * Sends the players the start countdown.
     */
    private void sendTimeToStart() {
        sendMessage(MessageUtils.message(ChatColor.GREEN + "Die Runde beginnt in " + ChatColor.GOLD + time + ChatColor.GREEN + " Sekunden!"));
    }

    /**
     * Sends the players the endRound countdown.
     */
    private void sendTimeToEnd() {
        sendMessage(MessageUtils.message(ChatColor.GREEN + "Die Runde endet in " + ChatColor.GOLD + time + ChatColor.GREEN + " Sekunden!"));
    }

    /**
     * Creates a message that someone has won versus the given player.
     * @param versus the player who lost
     * @return the message
     */
    private String getWonMessage(Player versus) {
        return MessageUtils.message(ChatColor.GREEN + "Du hast gegen " + versus.getName() + " gewonnen!");
    }

    /**
     * Creates a message that someone has lost versus the given player.
     * @param versus the player who won
     * @return the message
     */
    private String getLostMessage(Player versus) {
        return MessageUtils.message(ChatColor.GREEN + "Du hast gegen " + versus.getName() + " verloren!");
    }

    /**
     * Sends the players the messages that the runner has won.
     */
    private void sendRunnerWon() {
        getRunnerPlayer().sendMessage(getWonMessage(getCatcherPlayer()));
        getCatcherPlayer().sendMessage(getLostMessage(getRunnerPlayer()));
    }

    /**
     * Sends the players the messages that the catcher has won.
     */
    private void sendCatcherWon() {
        getCatcherPlayer().sendMessage(getWonMessage(getRunnerPlayer()));
        getRunnerPlayer().sendMessage(getLostMessage(getCatcherPlayer()));
    }

    /**
     * Sends the players the messages that it's a draw.
     */
    private void sendItsADraw() {
        sendMessage(MessageUtils.message(ChatColor.GREEN + "Unentschieden!"));
    }

    /**
     * Sends the players that the other left.
     */
    private void sendEnemyLeft() {
        sendMessage(MessageUtils.message(ChatColor.GREEN + "Dein Gegner hat das Spiel verlassen"));
    }

    /**
     * Starts a new round. Increments roundCount, sets the phase to START, teleports the players to the spawn and starts
     * the timer.
     */
    public void start() {
        roundCount++;
        currentPhase = Phase.START;

        if(hitBlock != null) {
            hitBlock.stop();
            hitBlock = null;
        }

        getRunnerPlayer().teleport(gameMap.getSpawn());
        getCatcherPlayer().teleport(gameMap.getSpawn());

        time = TIME_TO_START;
        sendRoles();
        sendTimeToStart();
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                time--;
                if (time == 0) {
                    task.cancel();
                    task = null;
                    gameStart(); // game can start now, because the time is 0
                } else {
                    sendTimeToStart();
                }
            }
        }, 20L, 20L);
    }

    /**
     * Starts the round. Teleports the players to their spawns and starts the timer.
     */
    public void gameStart() {
        sendMessage(MessageUtils.message(ChatColor.GREEN + "Die Runde beginnt!"));

        getRunnerPlayer().teleport(gameMap.getBases().get(0).getSpawn());
        getCatcherPlayer().teleport(gameMap.getCatcherSpawn());

        startInBase(0); // the runner has to start in the first base

        time = TIME_PER_ROUND;
        sendTimeToEnd();
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                time--;
                if (time == 0) {
                    task.cancel();
                    task = null;
                    endRound(); // game can end now, because the time is 0
                } else if (time % 60 == 0 || (time <= 30 && time % 10 == 0) || (time <= 3)) {
                    sendTimeToEnd();
                }
            }
        }, 20L, 20L);
    }

    /**
     * Ends the current round. If only one has been played, the players change the roles and start again. Otherwise
     * the match gets stopped.
     */
    private void endRound() {
        getRunnerPlayer().getInventory().clear();
        getCatcherPlayer().getInventory().clear();

        if (roundCount == 1) {
            changeRoles();
            start();
        } else {
            stop();

            IngameStats runnerStats = ingameStats.get(getRunner());
            IngameStats catcherStats = ingameStats.get(getCatcher());

            if (runnerStats.getFinishedRuns() > catcherStats.getFinishedRuns()) {
                runnerStats.won();
                sendRunnerWon();
            } else if (runnerStats.getFinishedRuns() < catcherStats.getFinishedRuns()) {
                catcherStats.won();
                sendCatcherWon();
            } else {
                sendItsADraw();
            }
        }
    }

    /**
     * Stops this round.
     */
    public void stop() {
        Main.getGameManager().stopMatch(this);

        if(hitBlock != null) {
            hitBlock.stop();
            hitBlock = null;
        }

        if(task != null)
            task.cancel();

        new Thread(new Runnable() {
            @Override
            public void run() {
                insertStatsIntoDatabase();
            }
        }).start();
    }

    /**
     * Called when a participant lefts.
     */
    public void playerLeft() {
        stop();
        sendEnemyLeft();

        getRunnerPlayer().getInventory().clear();
        getCatcherPlayer().getInventory().clear();
    }

    /**
     * Changes the roles of the catcher and the runner.
     */
    private void changeRoles() {
        UUID newRunner = catcher;
        catcher = runner;
        runner = newRunner;
    }

    /**
     * Let the runner start in the base with the given index. Sets the phase to SHOOTING and gives the runner his bow.
     *
     * @param newBase the index of the base
     */
    public void startInBase(int newBase) {
        // Clear the players to heal them etc.:
        PlayerUtils.clear(getRunnerPlayer());
        PlayerUtils.clear(getCatcherPlayer());

        if(hitBlock != null) {
            hitBlock.stop();
            hitBlock = null;
        }

        currentPhase = Phase.SHOOTING;

        if (newBase > currentBase)
            ingameStats.get(getRunner()).incrementFinishedRuns();

        // Change currentBase and close the open gates:
        gameMap.getBases().get(currentBase).closeExit();
        currentBase = newBase;
        gameMap.getBases().get(currentBase).closeEntrance();

        Player runner = getRunnerPlayer();
        runner.getInventory().addItem(new ItemStack(Material.BOW));
        runner.getInventory().addItem(new ItemStack(Material.ARROW));
        runner.updateInventory();

        // Maybe the catcher has a nether star, clear it:
        getCatcherPlayer().getInventory().clear();
    }

    /**
     * Called when the runner has hit a block with the arrow.
     *
     * @param block the hit block
     */
    public void runnerHitBlock(Block block) {

        if (gameMap.getValidArrowArea().containsBlockLocation(block.getLocation())) {
            // the runner has hit the valid area:

            hitBlock = new HitBlock(block);
            hitBlock.start();
            startRunToNextBase();

            ingameStats.get(getRunner()).incrementShootedArrows();
            ingameStats.get(getCatcher()).incrementEnemyShootedArrows();

        } else {
            // the runner has not hit the valid area, give him a new arrow:
            getRunnerPlayer().getInventory().addItem(new ItemStack(Material.ARROW));
            getRunnerPlayer().updateInventory();
        }
    }

    /**
     * Called when the catcher clicked on the hit block. The hit block gets removed and the catcher gets the nether star.
     */
    public void catcherClickedBlock() {
        hitBlock.stop();
        hitBlock = null;

        catcherGotBall();
    }

    /**
     * Called when the catcher got the 'ball'. He gets a nether star.
     */
    public void catcherGotBall() {
        getCatcherPlayer().getInventory().addItem(new ItemStack(Material.NETHER_STAR));
        getCatcherPlayer().updateInventory();
    }

    /**
     * Called when the runner shooted the arrow. This changes the phase to RUNNING and opens the gates.
     */
    private void startRunToNextBase() {
        currentPhase = Phase.RUNNING;
        gameMap.getBases().get(currentBase).openExit();
        gameMap.getBases().get(nextBaseIndex()).openEntrance();

        // clear the bow from his inventory:
        getRunnerPlayer().getInventory().clear();

        ingameStats.get(getRunner()).incrementStartedRuns();
    }

    /**
     * Called when the catcher inserted a nether star into the hopper. The runner has to start again from the last base.
     */
    public void ballInsertedIntoHopper() {
        startInBase(currentBase);
        getRunnerPlayer().teleport(gameMap.getBases().get(currentBase).getSpawn());

        ingameStats.get(getCatcher()).incrementGotArrows();
        ingameStats.get(getRunner()).incrementEnemyGotArrows();
    }

    /**
     * Called when the catcher dies. He gets teleported to his spawn and the runner can start at the next base.
     */
    public void catcherDied() {
        getCatcherPlayer().teleport(gameMap.getCatcherSpawn());

        startInBase(nextBaseIndex());
        getRunnerPlayer().teleport(gameMap.getBases().get(currentBase).getSpawn());
    }

    /**
     * Called when the runner dies. He has to start again from the last base.
     */
    public void runnerDied() {
        startInBase(currentBase);
        getRunnerPlayer().teleport(gameMap.getBases().get(currentBase).getSpawn());
    }

    /**
     * Calculates the index of the next base for the runner.
     * @return the index of the next base
     */
    public int nextBaseIndex() {
        if (currentBase == gameMap.getBases().size() - 1) {
            // last base, return two the first
            return 0;
        } else {
            // not last base, simply add 1 to the index
            return currentBase + 1;
        }
    }

    private void insertStatsIntoDatabase() {
        try {
            Statement statement = Main.getPluginDatabase().createStatement();

            // insert match
            String sql = "INSERT INTO matches (mapName) VALUES ('" + getGameMap().getName() + "')";
            System.out.println(sql);
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            // get match id
            int matchID = 0;
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) matchID = resultSet.getInt(1);

            ingameStats.get(getRunner()).insertIntoDatabase(statement, getRunner(), matchID);
            ingameStats.get(getCatcher()).insertIntoDatabase(statement, getCatcher(), matchID);

            statement.close();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
