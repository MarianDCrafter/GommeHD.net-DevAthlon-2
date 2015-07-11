package io.github.mariandcrafter.devathlon2.runde1;

import io.github.mariandcrafter.devathlon2.runde1.commands.StartGameCommands;
import io.github.mariandcrafter.devathlon2.runde1.game.GameManager;
import io.github.mariandcrafter.devathlon2.runde1.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {

    private static Main instance;
    private static Configuration configuration;
    private static GameManager gameManager;

    private List<Listener> listeners;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
        loadGameManager();
        loadListeners();
        loadCommands();

        System.out.println("2. Devathlon, 1. Runde - GreenGlowPixel-Team - Plugin enabled!");
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
        getConfig().options().copyDefaults(true);
        configuration = new Configuration(getConfig());
        // TODO config can not be changed with these statements
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
        Bukkit.getPluginManager().registerEvents(new ArrowListener(), this);
        Bukkit.getPluginManager().registerEvents(new BallClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new ItemPickupDropListener(), this);
        Bukkit.getPluginManager().registerEvents(new HopperPickupListener(), this);
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

}
