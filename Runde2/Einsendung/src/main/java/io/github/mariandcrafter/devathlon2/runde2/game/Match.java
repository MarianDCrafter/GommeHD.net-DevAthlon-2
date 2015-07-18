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
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A match between two players.
 */
public class Match {

    public enum Phase {
        /**
         * The game has not started yet but the timer is running.
         */
        STARTING,
        /**
         * The 'normal' phase. The catcher is trying to kill the runner and the runner tries to collect the armor or
         * tries to run to the rescue capsule.
         */
        RUNNING,
        /**
         * The rescue capsule is currently in use by the catcher or the runner. This phase is three seconds long.
         */
        RESCUE_CAPSULE,
        /**
         * The catcher or the runner is falling into the void. This phase is three seconds long.
         */
        VOID
    }

    /**
     * The map of this match.
     */
    private GameMap gameMap;
    /**
     * The UUID of the runner.
     */
    private UUID runner;
    /**
     * The UUID of the catcher.
     */
    private UUID catcher;

    /**
     * The current phase of the match. See in the enum above.
     */
    private Phase phase;
    /**
     * The times when the players have used the pressure plates. Used to check whether a player can use again a pressure
     * plate, which is every 60 seconds possible.
     */
    private Map<UUID, Long> pressurePlateUseTimes = new HashMap<UUID, Long>();

    /**
     * The current time for the task.
     */
    private int time;
    /**
     * The current used task. This can be the task for
     * the start timer (in phase STARTING),
     * the rescue capsule (in phase RESCUE_CAPSULE) or
     * the void (in phase VOID).
     * It's not possible that multiple timers are running, because the start timer starts only at the start of the match,
     * and the other timers can only start in the RUNNING phase. That's why no timer can start while another is running.
     */
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
     * @return the current match phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * @return the pressure plate start times
     */
    public Map<UUID, Long> getPressurePlateUseTimes() {
        return pressurePlateUseTimes;
    }

    /**
     * Starts a new round.
     */
    public void start() {
        phase = Phase.STARTING;

        // Teleport the players to their spawn:
        getCatcherPlayer().teleport(gameMap.getCatcherSpawn());
        getRunnerPlayer().teleport(gameMap.getRunnerSpawn());

        // Clear the players:
        PlayerUtils.clear(getRunnerPlayer());
        PlayerUtils.clear(getCatcherPlayer());

        // Prepare the map:
        gameMap.spawnArmorStands();
        gameMap.getRescueCapsule().openEntrance();
        gameMap.getRescueCapsule().closeExit();

        // Give the players their items:
        giveCatcherSword();
        giveRunnerKnockbackStick();
        giveRunnerArmorCompass();
        updateRunnerCompass();

        sendStartMessages();

        // Start timer:
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

                    // Start the game:
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

    /**
     * Sends the catcher and the runner the start messages with the map information, the enemy and the role.
     */
    private void sendStartMessages() {
        getRunnerPlayer().sendMessage("");
        getCatcherPlayer().sendMessage("");

        // Send map information:
        MessageUtils.info("Du spielst auf der Map " + gameMap.getName() + " von " + gameMap.getCreator(), getRunnerPlayer(), getCatcherPlayer());

        // Send enemy:
        MessageUtils.info("Dein Gegner ist " + getCatcherPlayer().getName(), getRunnerPlayer());
        MessageUtils.info("Dein Gegner ist " + getRunnerPlayer().getName(), getCatcherPlayer());

        // Send role:
        MessageUtils.success("Du bist der Runner. Suche die 4 Rüstungsteile und gehe danach zur " +
                "Rettungskapsel. Pass auf, dass du nicht vom Catcher gekillt wirst.", getRunnerPlayer());
        MessageUtils.success("Du bist der Catcher. Finde den Runner und kill ihn, um zu gewinnen.", getCatcherPlayer());
    }

    /**
     * Stops this round. Cancels the timer if necessary and removes the match from the game manager.
     */
    public void stop() {
        if (task != null)
            task.cancel();
        gameMap.getRescueCapsule().stop();

        Main.getGameManager().stopMatch(this);
    }

    /**
     * Called when a participant lefts. Used to stop the game and notify the other player about it.
     */
    public void playerLeft() {
        stop();

        // Simply send both players the message, because we don't know who left and the player who left will not
        // see it.
        MessageUtils.error("Dein Gegner hat das Spiel verlassen.", getRunnerPlayer(), getCatcherPlayer());
    }

    /**
     * Called when the catcher kills the runner. Used to stop the game, send the winner messages and play sounds.
     */
    public void catcherKilledRunner() {
        stop();

        MessageUtils.success("Du hast den Runner gekillt und gewonnen!", getCatcherPlayer());
        MessageUtils.info("Du wurdest vom Catcher gekillt und hast verloren!", getRunnerPlayer());

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
     * Gives the runner a knockback stick.
     */
    public void giveRunnerKnockbackStick() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        getRunnerPlayer().getInventory().addItem(itemStack);
        getRunnerPlayer().updateInventory();
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
        // Clear the inventory of the runner, because he should already have the other compass. This does not effect the armor.
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

        if (!runnerHasCompleteArmor()) {

            // Calculate the location of the nearest armor stand:
            Location nearest = null;
            double shortestDistance = Double.MAX_VALUE;

            for (ArmorStand armorStand : gameMap.getArmorStands()) {
                double distance = armorStand.getLocation().distance(runner.getLocation());
                if ((nearest == null || distance < shortestDistance) && armorStand.getLocation() != null) {
                    // armorStand is the nearest armor stand until now:
                    nearest = armorStand.getLocation();
                    shortestDistance = distance;
                }
            }

            // Set the compass target to the calculated location:
            runner.setCompassTarget(nearest);

        } else {
            // The runner has the whole armor, he can go to the rescue capsule:
            runner.setCompassTarget(gameMap.getRescueCapsule().getRescueButtonLocation());
        }
    }

    /**
     * Checks whether the runner has the whole armor.
     * @return {@code true} if the runner has the whole armor, otherwise {@code false}
     */
    public boolean runnerHasCompleteArmor() {
        PlayerInventory inventory = getRunnerPlayer().getInventory();
        return inventory.getHelmet() != null && inventory.getChestplate() != null &&
                inventory.getLeggings() != null && inventory.getBoots() != null;
    }

    /**
     * Called when the runner uses the rescue capsule while he is wearing the whole armor.
     * Sets phase to RESCUE_CAPSULE, sends messages and starts the rescue capsule.
     */
    public void runnerUsedRescueCapsuleWithArmor() {
        phase = Phase.RESCUE_CAPSULE;

        MessageUtils.success("Du hast die Rettungskapsel erfolgreich abgeschossen!", getRunnerPlayer());
        MessageUtils.error("Der Runner hat die Rettungskapsel abgeschossen!", getCatcherPlayer());

        gameMap.getRescueCapsule().usedByPlayerWithArmor(getRunnerPlayer());

        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                MessageUtils.success("Du bist erfolgreich mit der Rettungskapsel gelandet und hast gewonnen!", getRunnerPlayer());
                MessageUtils.error("Der Runner ist mit der Rettungskapsel gelandet. Du hast verloren.", getCatcherPlayer());

                stop();
            }
        }, 60L);
    }

    /**
     * Called when the catcher uses the rescue capsule while he is not wearing the whole armor.
     * Sets phase to RESCUE_CAPSULE, sends messages and starts the rescue capsule.
     */
    public void runnerUsedRescueCapsuleWithoutFullArmor() {
        phase = Phase.RESCUE_CAPSULE;

        MessageUtils.error("Du hast die Rettungskapsel abgeschossen, ohne die volle Astronautenrüstung anzuhaben!", getRunnerPlayer());
        MessageUtils.success("Der Runner hat die Rettungskapsel abgeschossen, ohne die volle Astronautenrüstung anzuhaben.", getCatcherPlayer());

        gameMap.getRescueCapsule().usedByPlayerWithoutArmor(getRunnerPlayer());

        task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                MessageUtils.error("Du bist leider in der Rettungskapsel wegen mangelnder Astronautenrüstung erstickt und hast verloren!", getRunnerPlayer());
                MessageUtils.success("Der Runner ist in der Rettungskapsel wegen mangelnder Astronautenrüstung erstickt. Du hast gewonnen.", getCatcherPlayer());

                stop();
            }
        }, 60L);
    }

    /**
     * Called when the runner falls into the void.
     * Sets phase to VOID, sends messages and stops the match after three seconds.
     */
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

    /**
     * Called when the catcher falls into the void.
     * Sets phase to VOID, sends messages and stops the match after three seconds.
     */
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
