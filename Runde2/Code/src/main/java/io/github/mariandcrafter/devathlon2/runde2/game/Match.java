package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        gameMap.getRescueCapsule().openEntrance();
        gameMap.getRescueCapsule().closeExit();
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

        // TODO send message
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

    /**
     * @return {@code true} if the runner has the whole armor, otherwise {@code false}
     */
    public boolean runnerHasCompleteArmor() {
        PlayerInventory inventory = getRunnerPlayer().getInventory();
        return inventory.getHelmet() != null && inventory.getChestplate() != null &&
                inventory.getLeggings() != null && inventory.getBoots() != null;
    }

    /**
     * Called when the runner uses the rescue capsule while he is wearing the whole armor.
     */
    public void runnerUsedRescueCapsule() {
        gameMap.getRescueCapsule().closeEntrance();
        gameMap.getRescueCapsule().openExit();

        MessageUtils.success(getRunnerPlayer(), "Du hast die Rettungskapsel erfolgreich abgeschossen!");
        MessageUtils.error(getCatcherPlayer(), "Der Runner hat die Rettungskapsel abgeschossen!");

        getRunnerPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));
        getRunnerPlayer().getLocation().getWorld().playSound(getRunnerPlayer().getLocation(), Sound.FIREWORK_LAUNCH, 50, 1);
        getRunnerPlayer().getLocation().getWorld().playEffect(getRunnerPlayer().getLocation(), Effect.EXPLOSION_HUGE, 20);

        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                runnerUsedRescueCapsuleApplyNightVision();
            }
        }, 20L);
    }

    /**
     * Called when the blindess effect has finished. Applys a night vision effect.
     */
    private void runnerUsedRescueCapsuleApplyNightVision() {
        getRunnerPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 40, 1));
        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                runnerUsedRescueCapsuleFinished();
            }
        }, 40L);
    }

    /**
     * Called when all effects of the rescue capsule are finished. Stops the match.
     */
    private void runnerUsedRescueCapsuleFinished() {
        MessageUtils.success(getRunnerPlayer(), "Du bist erfolgreich mit der Rettungskapsel gelandet und hast gewonnen!");
        MessageUtils.error(getCatcherPlayer(), "Der Runner ist mit der Rettungskapsel gelandet. Du hast verloren.");

        stop();
    }

    public void runnerUsedRescueCapsuleWithoutFullArmor() {
        gameMap.getRescueCapsule().closeEntrance();
        gameMap.getRescueCapsule().openExit();

        MessageUtils.error(getRunnerPlayer(), "Du hast die Rettungskapsel abgeschossen, ohne die volle Astronautenrüstung anzuhaben!");
        MessageUtils.success(getCatcherPlayer(), "Der Runner hat die Rettungskapsel abgeschossen, ohne die volle Astronautenrüstung anzuhaben.");

        getRunnerPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
        getRunnerPlayer().getLocation().getWorld().playSound(getRunnerPlayer().getLocation(), Sound.FIREWORK_LAUNCH, 50, 1);
        getRunnerPlayer().getLocation().getWorld().playEffect(getRunnerPlayer().getLocation(), Effect.EXPLOSION_HUGE, 20);

        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                runnerUsedRescueCapsuleWithoutFullArmorStartDamage();
            }
        }, 20L);
    }

    private void runnerUsedRescueCapsuleWithoutFullArmorStartDamage() {
        time = 40;

        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                time--;

                if (time <= 0) {
                    task.cancel();
                    task = null;
                    runnerUsedRescueCapsuleWithoutFullArmorFinished();
                } else {
                    getRunnerPlayer().damage(0);
                }
            }
        }, 1L, 1L);
    }

    private void runnerUsedRescueCapsuleWithoutFullArmorFinished() {
        MessageUtils.error(getRunnerPlayer(), "Du bist leider in der Rettungskapsel wegen mangelnder Astronautenrüstung erstickt und hast verloren!");
        MessageUtils.success(getCatcherPlayer(), "Der Runner ist in der Rettungskapsel wegen mangelnder Astronautenrüstung erstickt. Du hast gewonnen.");

        stop();
    }

}
