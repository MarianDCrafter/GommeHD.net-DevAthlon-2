package io.github.mariandcrafter.devathlon2.runde2;

import io.github.mariandcrafter.devathlon2.runde2.commands.StartGameCommands;
import io.github.mariandcrafter.devathlon2.runde2.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Configuration configuration;
    private static GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
        loadGameManager();
        loadListeners();
        loadCommands();
        removeEntities();

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
     * Sets difficulty to PEACEFUL and removes all entities in all maps.
     */
    public void removeEntities() {
        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(Difficulty.PEACEFUL);
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

}
