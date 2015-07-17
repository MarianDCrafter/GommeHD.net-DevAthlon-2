package io.github.mariandcrafter.devathlon2.runde2;

import io.github.mariandcrafter.devathlon2.runde2.commands.StartGameCommands;
import io.github.mariandcrafter.devathlon2.runde2.game.GameManager;
import io.github.mariandcrafter.devathlon2.runde2.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Configuration configuration;
    private static GameManager gameManager;
    private static Random random = new Random();

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
        loadGameManager();
        loadListeners();
        loadCommands();
        setupWorlds();

        System.out.println("2. Devathlon, 2. Runde - GreenGlowPixel-Team - Plugin enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("2. Devathlon, 1. Runde - GreenGlowPixel-Team - Plugin disabled!");
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
            gameManager.onJoin(player);
        }
    }

    /**
     * Registers every Listener.
     */
    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new ItemDropListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorStandListener(), this);
        Bukkit.getPluginManager().registerEvents(new HungerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        // TODO Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ButtonClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
    }

    /**
     * Sets the command executors of the used commands in this plugin.
     */
    public void loadCommands() {
        // invite/accept/deny are used by StartGameCommands
        StartGameCommands startGameCommands = new StartGameCommands();
        getCommand("invite").setExecutor(startGameCommands);
        getCommand("accept").setExecutor(startGameCommands);
        getCommand("deny").setExecutor(startGameCommands);
        getCommand("loc").setExecutor(startGameCommands); // TODO remove!
    }

    /**
     * Removes all entites, sets the diffculty to PEACEFUL and disables the daylight cycle in all maps.
     */
    public void setupWorlds() {
        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setTime(18000L);
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Player))
                    entity.remove();
            }
        }
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

    /**
     * @return the Random instance used by the whole plugin
     */
    public static Random getRandom() {
        return random;
    }
}
