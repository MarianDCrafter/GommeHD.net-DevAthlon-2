package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.utils.ItemUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A match between two players.
 */
public class Match {

    public enum Phase {
        STARTING, RUNNING, RESCUE_CAPSULE, VOID
    }

    private GameMap gameMap;
    private UUID runner;
    private UUID catcher;

    private Phase phase;
    private Map<UUID, Long> pressurePlateStartTimes = new HashMap<UUID, Long>();

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

    public Phase getPhase() {
        return phase;
    }

    /**
     * @return the pressure plate start times
     */
    public Map<UUID, Long> getPressurePlateStartTimes() {
        return pressurePlateStartTimes;
    }

    /**
     * Starts a new round.
     */
    public void start() {
        getCatcherPlayer().teleport(gameMap.getCatcherSpawn());
        getRunnerPlayer().teleport(gameMap.getRunnerSpawn());

        gameMap.fillArmorStands();
        gameMap.getRescueCapsule().openEntrance();
        gameMap.getRescueCapsule().closeExit();

        giveCatcherSword();
        giveRunnerArmorCompass();
        updateRunnerCompass();

        sendStartMessages();

        phase = Phase.STARTING;

        time = 11;
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                time--;

                Player runner = getRunnerPlayer();
                Player catcher = getCatcherPlayer();

                if (time <= 0) {
                    task.cancel();
                    task = null;
                    phase = Phase.RUNNING;
                    MessageUtils.info("Das Spiel startet jetzt!", runner, catcher);
                    PlayerUtils.playSound(Sound.NOTE_PLING, 10, 1, runner, catcher);

                } else if (time == 10 || time <= 5) {
                    MessageUtils.info("Das Spiel startet in " + time + " Sekunden.", runner, catcher);
                    PlayerUtils.playSound(Sound.NOTE_PIANO, 10, 1, runner, catcher);
                }
            }
        }, 20L, 20L);
    }

    private void sendStartMessages() {
        getRunnerPlayer().sendMessage("");
        getCatcherPlayer().sendMessage("");

        MessageUtils.info("Du spielst auf der Map " + gameMap.getName() + " von " + gameMap.getCreator(), getRunnerPlayer(), getCatcherPlayer());

        MessageUtils.info("Dein Gegner ist " + getCatcherPlayer().getName(), getRunnerPlayer());
        MessageUtils.info("Dein Gegner ist " + getRunnerPlayer().getName(), getCatcherPlayer());

        MessageUtils.success("Du bist der Runner. Suche die 4 Rüstungsteile und gehe danach zur " +
                "Rettungskapsel. Pass auf, dass du nicht vom Catcher gekillt wirst.", getRunnerPlayer());
        MessageUtils.success("Du bist der Catcher. Finde den Runner und kill ihn, um zu gewinnen.", getCatcherPlayer());
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

        MessageUtils.error("Dein Gegner hat das Spiel verlassen.", getRunnerPlayer(), getCatcherPlayer());
    }

    public void catcherKilledRunner() {
        MessageUtils.success("Du hast den Runner gekillt und gewonnen!", getCatcherPlayer());
        MessageUtils.info("Du wurdest vom Catcher gekillt und hast verloren!", getRunnerPlayer());

        stop();

        PlayerUtils.playSound(Sound.LEVEL_UP, 10, 1, getCatcherPlayer());
        PlayerUtils.playSound(Sound.CAT_MEOW, 10, 1, getRunnerPlayer());
    }

    /**
     * Gives the catcher a gold sword.
     */
    public void giveCatcherSword() {
        ItemStack itemStack = new ItemStack(Material.GOLD_SWORD);
        itemStack.addEnchantment(Enchantment.DURABILITY, 3);
        getCatcherPlayer().getInventory().addItem(itemStack);
        getCatcherPlayer().updateInventory();
    }

    /**
     * Gives the runner a compass which points to the nearest armor part.
     */
    public void giveRunnerArmorCompass() {
        getRunnerPlayer().getInventory().addItem(ItemUtils.createItemStackWithName(Material.COMPASS, ChatColor.GOLD + "Nächstes Rüstungsteil"));
        getRunnerPlayer().updateInventory();
    }

    /**
     * Gives the runner a compass which points to the rescue capsule.
     */
    public void giveRunnerRescueCapsuleCompass() {
        getRunnerPlayer().getInventory().clear();

        getRunnerPlayer().getInventory().addItem(ItemUtils.createItemStackWithName(Material.COMPASS, ChatColor.GOLD + "Rettungskapsel"));
        getRunnerPlayer().updateInventory();
    }

    /**
     * Updates the compass target location of the runner. If the runner has collected all armor parts, the compass
     * points to the rescue capsule, otherwise to the nearest armor stand.
     */
    public void updateRunnerCompass() {
        Player runner = getRunnerPlayer();

        if(!runnerHasCompleteArmor()) {

            Location nearest = null;
            double shortestDistance = Double.MAX_VALUE;

            for (ArmorStand armorStand : gameMap.getArmorStands()) {
                double distance = armorStand.getLocation().distance(runner.getLocation());
                if (nearest == null || distance < shortestDistance && armorStand.getLocation() != null) {
                    nearest = armorStand.getLocation();
                    shortestDistance = distance;
                }
            }
            runner.setCompassTarget(nearest);

        } else {
            runner.setCompassTarget(gameMap.getRescueCapsule().getRescueButtonLocation());
        }
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
        phase = Phase.RESCUE_CAPSULE;

        gameMap.getRescueCapsule().closeEntrance();
        gameMap.getRescueCapsule().openExit();

        MessageUtils.success("Du hast die Rettungskapsel erfolgreich abgeschossen!", getRunnerPlayer());
        MessageUtils.error("Der Runner hat die Rettungskapsel abgeschossen!", getCatcherPlayer());

        getRunnerPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 50));
        PlayerUtils.playSound(getRunnerPlayer().getLocation(), Sound.FIREWORK_LAUNCH, 50, 1);

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
        MessageUtils.success("Du bist erfolgreich mit der Rettungskapsel gelandet und hast gewonnen!", getRunnerPlayer());
        MessageUtils.error("Der Runner ist mit der Rettungskapsel gelandet. Du hast verloren.", getCatcherPlayer());

        stop();
    }

    public void runnerUsedRescueCapsuleWithoutFullArmor() {
        phase = Phase.RESCUE_CAPSULE;

        gameMap.getRescueCapsule().closeEntrance();
        gameMap.getRescueCapsule().openExit();

        MessageUtils.error("Du hast die Rettungskapsel abgeschossen, ohne die volle Astronautenrüstung anzuhaben!", getRunnerPlayer());
        MessageUtils.success("Der Runner hat die Rettungskapsel abgeschossen, ohne die volle Astronautenrüstung anzuhaben.", getCatcherPlayer());

        getRunnerPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 50));
        PlayerUtils.playSound(getRunnerPlayer().getLocation(), Sound.FIREWORK_LAUNCH, 50, 1);

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
        MessageUtils.error("Du bist leider in der Rettungskapsel wegen mangelnder Astronautenrüstung erstickt und hast verloren!", getRunnerPlayer());
        MessageUtils.success("Der Runner ist in der Rettungskapsel wegen mangelnder Astronautenrüstung erstickt. Du hast gewonnen.", getCatcherPlayer());

        stop();
    }

    public void runnerFallingIntoVoid() {
        phase = Phase.VOID;

        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                MessageUtils.error("Du bist aus dem Raumschiff gefallen und hast verloren.", getRunnerPlayer());
                MessageUtils.success("Der Runner ist aus dem Raumschiff gefallen. Du hast gewonnen.", getCatcherPlayer());

                stop();
            }
        }, 60L);
    }

    public void catcherFallingIntoVoid() {
        phase = Phase.VOID;

        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                MessageUtils.error("Du bist aus dem Raumschiff gefallen und hast verloren.", getCatcherPlayer());
                MessageUtils.success("Der Catcher ist aus dem Raumschiff gefallen. Du hast gewonnen.", getRunnerPlayer());

                stop();
            }
        }, 60L);
    }

}
