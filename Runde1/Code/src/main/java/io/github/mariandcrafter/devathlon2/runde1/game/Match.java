package io.github.mariandcrafter.devathlon2.runde1.game;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde1.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

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
     * Sends the players the end countdown.
     */
    private void sendTimeToEnd() {
        sendMessage(MessageUtils.message(ChatColor.GREEN + "Die Runde endet in " + ChatColor.GOLD + time + ChatColor.GREEN + " Sekunden!"));
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
                    end(); // game can end now, because the time is 0
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
    private void end() {
        if (roundCount == 1) {
            changeRoles();
            start();
        } else {
            Main.getGameManager().stopMatch(this);

            if(hitBlock != null) {
                hitBlock.stop();
                hitBlock = null;
            }
        }
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

        // Change currentBase and close the open gates:
        gameMap.getBases().get(currentBase).closeExit();
        currentBase = newBase;
        gameMap.getBases().get(currentBase).closeEntrance();

        Player runner = getRunnerPlayer();
        runner.getInventory().addItem(new ItemStack(Material.BOW));
        runner.getInventory().addItem(new ItemStack(Material.ARROW));

        // Maybe the catcher has a nether star, clear it:
        getCatcherPlayer().getInventory().clear();

        // TODO sicherheitshalber inventory clearen, level zurücksetzen, kein feuer, keine Effekte, heilen, etc.
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

        } else {
            // the runner has not hit the valid area, give him a new arrow:
            getRunnerPlayer().getInventory().addItem(new ItemStack(Material.ARROW));
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
    }

    /**
     * Called when the catcher inserted a nether star into the hopper. The runner has to start again from the last base.
     */
    public void ballInsertedIntoHopper() {
        startInBase(currentBase);
        getRunnerPlayer().teleport(gameMap.getBases().get(currentBase).getSpawn());
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

}
