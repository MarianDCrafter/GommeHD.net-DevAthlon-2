package io.github.mariandcrafter.devathlon2.runde1.game;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Match {

    public enum Phase {
        START, SHOOTING, RUNNING, END
    }

    private final static int TIME_TO_START = 200;
    private final static int TIME_PER_ROUND = 2400;

    private GameMap gameMap;
    private UUID runner;
    private UUID catcher;

    private int time = TIME_TO_START;
    private BukkitTask task;

    private Phase currentPhase;
    private int currentBase = 0;

    private Material hitBlockBeforeMaterial;
    private byte hitBlockBeforeData = 0;
    private Block hitBlock;

    public Match(GameMap gameMap, UUID runner, UUID catcher) {
        this.gameMap = gameMap;
        this.runner = runner;
        this.catcher = catcher;

        gameMap.closeAllGates();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public UUID getRunner() {
        return runner;
    }

    public Player getRunnerPlayer() {
        return Bukkit.getPlayer(runner);
    }

    public UUID getCatcher() {
        return catcher;
    }

    public Player getCatcherPlayer() {
        return Bukkit.getPlayer(catcher);
    }

    public void start() {
        currentPhase = Phase.START;

        getRunnerPlayer().teleport(gameMap.getSpawn());
        getCatcherPlayer().teleport(gameMap.getSpawn());

        sendTimeToStart();
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                time--;
                if (time == 0) {
                    task.cancel();
                    gameStart();
                } else if (time % 20 == 0) {
                    sendTimeToStart();
                }
            }
        }, 1L, 1L);
    }

    private void sendMessage(String message) {
        getRunnerPlayer().sendMessage(message);
        getCatcherPlayer().sendMessage(message);
    }

    private void sendTimeToStart() {
        sendMessage(Message.message(ChatColor.GREEN + "Die Runde beginnt in " + ChatColor.GOLD + (time / 20) + ChatColor.GREEN + " Sekunden!"));
    }

    private void sendTimeToEnd() {
        sendMessage(Message.message(ChatColor.GREEN + "Die Runde endet in " + ChatColor.GOLD + (time / 20) + ChatColor.GREEN + " Sekunden!"));
    }

    public void gameStart() {
        sendMessage(Message.message(ChatColor.GREEN + "Die Runde beginnt!"));

        getRunnerPlayer().teleport(gameMap.getBases().get(0).getSpawn());
        getCatcherPlayer().teleport(gameMap.getCatcherSpawn());

        startInBase(0);

        time = TIME_PER_ROUND;
        sendTimeToEnd();
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                time--;

                if (time == 0) {
                    task.cancel();
                    end();
                } else {
                    if (time % 2 == 0) {
                        randomHitBlock();
                    }
                    if (time % 2400 == 0 || (time <= 600 && time % 200 == 0) || (time <= 60 && time % 20 == 0)) {
                        sendTimeToEnd();
                    }
                }
            }
        }, 1L, 1L);
    }

    private void randomHitBlock() {
        if (hitBlock != null) {
            hitBlock.setType(Material.WOOL);
            //noinspection deprecation
            hitBlock.setData((byte) (Math.random() * 16));
        }
    }

    public void startInBase(int newBase) {
        currentPhase = Phase.SHOOTING;

        gameMap.getBases().get(currentBase).closeExit();
        currentBase = newBase;
        gameMap.getBases().get(currentBase).closeEntrance();

        Player runner = getRunnerPlayer();
        runner.getInventory().addItem(new ItemStack(Material.BOW));
        runner.getInventory().addItem(new ItemStack(Material.ARROW));

        getCatcherPlayer().getInventory().clear();
    }

    public void runnerHitBlock(Arrow arrow, Block block) {
        if (currentPhase != Phase.SHOOTING || arrow.getShooter() != getRunnerPlayer()) return;
        arrow.remove();

        if (gameMap.getValidArrowArea().containsBlockLocation(block.getLocation())) {
            hitBlockBeforeMaterial = block.getType();
            //noinspection deprecation
            hitBlockBeforeData = block.getData();
            hitBlock = block;

            startRunToNextBase();
        } else {
            getRunnerPlayer().getInventory().addItem(new ItemStack(Material.ARROW));
        }
    }

    public boolean catcherHitByArrow(Arrow arrow) {
        if (currentPhase != Phase.SHOOTING) return false;
        arrow.remove();

        if (arrow.getShooter() == getRunnerPlayer()) {
            startRunToNextBase();
            catcherGotBall();
            return true;
        } else {
            return false;
        }
    }

    private void rollbackHitBlock() {
        hitBlock.setType(hitBlockBeforeMaterial);
        //noinspection deprecation
        hitBlock.setData(hitBlockBeforeData);

        hitBlockBeforeMaterial = null;
        hitBlockBeforeData = 0;
        hitBlock = null;
    }

    public void catcherClickedBlock(Block block) {
        if (!block.getLocation().equals(block.getLocation())) return;
        rollbackHitBlock();
        catcherGotBall();
    }

    public void catcherGotBall() {
        getCatcherPlayer().getInventory().addItem(new ItemStack(Material.NETHER_STAR));
    }

    private void startRunToNextBase() {
        currentPhase = Phase.RUNNING;
        gameMap.getBases().get(currentBase).openExit();
        gameMap.getBases().get(nextBaseIndex()).openEntrance();

        getRunnerPlayer().getInventory().clear();
    }

    public void ballInsertedIntoHopper() {
        getRunnerPlayer().teleport(gameMap.getBases().get(currentBase).getSpawn());
        startInBase(currentBase);
    }

    private void end() {

    }

    public int nextBaseIndex() {
        if (currentBase == gameMap.getBases().size() - 1) {
            return 0;
        } else {
            return currentBase + 1;
        }
    }

}
