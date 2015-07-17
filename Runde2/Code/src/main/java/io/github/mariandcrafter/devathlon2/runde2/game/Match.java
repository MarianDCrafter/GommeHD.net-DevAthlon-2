package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * A match between two players.
 */
public class Match {

    private GameMap gameMap;
    private UUID runner;
    private UUID catcher;

    private int time;
    private BukkitTask task;

    /**
     * @param gameMap the map for this match
     * @param runner  the uuid of the runner
     * @param catcher the uuid of the catcher
     */
    public Match(GameMap gameMap, UUID runner, UUID catcher) {
        this.gameMap = gameMap;
        this.runner = runner;
        this.catcher = catcher;

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
     * Starts a new round.
     */
    public void start() {
        getCatcherPlayer().teleport(gameMap.getCatcherSpawn());
        spawnRunner();

        gameMap.fillArmorStands();
    }

    public void catcherKilledRunner() {
        MessageUtils.success(getCatcherPlayer(), "Du hast den Runner gekillt und gewonnen!");
        MessageUtils.info(getRunnerPlayer(), "Du wurdest vom Catcher gekillt und hast verloren!");

        stop();
    }

    /**
     * Stops this round.
     */
    public void stop() {
        if (task != null)
            task.cancel();

        Main.getGameManager().stopMatch(this);
    }

    /**
     * Called when a participant lefts.
     */
    public void playerLeft() {
        stop();

        getRunnerPlayer().getInventory().clear();
        getCatcherPlayer().getInventory().clear();
    }

    /**
     * Teleports the runner to a random runner spawn.
     */
    public void spawnRunner() {
        // assuming the list contains at least one runner spawn:
        int index = Main.getRandom().nextInt(gameMap.getRunnerSpawns().size());
        Location location = gameMap.getRunnerSpawns().get(index);
        getRunnerPlayer().teleport(location);
    }

}
