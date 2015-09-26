package io.github.mariandcrafter.devathlon2.runde3;

import io.github.mariandcrafter.bukkitpluginapi.Plugin;
import io.github.mariandcrafter.bukkitpluginapi.messages.MessageSender;
import io.github.mariandcrafter.devathlon2.runde3.commands.OfferCommand;
import io.github.mariandcrafter.devathlon2.runde3.commands.StartCommand;
import io.github.mariandcrafter.devathlon2.runde3.game.GameManager;
import io.github.mariandcrafter.devathlon2.runde3.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * The main class of the DevAthlon plugin.
 *
 * @author MarianDCrafter
 */
public class Main extends Plugin {

    private static Main instance;
    private static Configuration configuration;
    private static GameManager gameManager;
    private static Random random = new Random();

    /**
     * Called when the plugin is enabled. Sets up everything including listeners, commands etc.
     */
    @Override
    public void onEnable() {
        instance = this;

        setupWorld();
        loadConfig();
        loadGameManager();
        loadListeners();
        loadCommands();

        System.out.println("2. Devathlon, 3. Runde - GreenGlowPixel-Team - Plugin enabled!");
    }

    private void setupWorld() {
        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(Difficulty.HARD);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("mobGriefing", "false");
            world.setTime(22500);
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Player)) {
                    entity.remove();
                }
            }
        }
    }

    /**
     * Creates an instance of the Configuration class with the configuration file.
     */
    public void loadConfig() {
        saveDefaultConfig();
        configuration = new Configuration(getConfig());
    }

    /**
     * Creates an instance of GameManager. If there are already players on the server, the GameManager gets notified
     * about it.
     */
    public void loadGameManager() {
        gameManager = new GameManager();

        for (Player player : Bukkit.getOnlinePlayers()) {
            gameManager.joinLobby(player);
        }
    }

    /**
     * Registers every Listener.
     */
    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new DisableMobSpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new SilverfishInfectListener(), this);
        Bukkit.getPluginManager().registerEvents(new VillagerTradeListener(), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new DoorListener(), this);
        Bukkit.getPluginManager().registerEvents(configuration.getWitchhunt(), this);
        Bukkit.getPluginManager().registerEvents(configuration.getArchery(), this);
    }

    /**
     * Sets the command executors of the used commands in this plugin.
     */
    public void loadCommands() {
        addCommands(
                new StartCommand(),
                new OfferCommand()
        );
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        System.out.println("2. Devathlon, 3. Runde - GreenGlowPixel-Team - Plugin disabled!");
    }

    /**
     * @return the instance of the main class
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * @return the created instance of the Configuration class
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * @return the GameManager used by the plugin
     */
    public static GameManager getGameManager() {
        return gameManager;
    }

    public static MessageSender messageSender() {
        return instance.getMessageSender();
    }

    /**
     * @return the Random instance used by the whole plugin
     */
    public static Random getRandom() {
        return random;
    }

    @Override
    protected String prefix() {
        return "Mittelalter";
    }

    @Override
    protected ChatColor defaultColor() {
        return ChatColor.GOLD;
    }

}
