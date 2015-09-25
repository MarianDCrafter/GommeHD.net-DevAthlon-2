package io.github.mariandcrafter.devathlon2.runde3;

import io.github.mariandcrafter.bukkitpluginapi.Plugin;
import io.github.mariandcrafter.bukkitpluginapi.messages.MessageSender;
import io.github.mariandcrafter.devathlon2.runde3.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * The main class of the DevAthlon plugin.
 *
 * @author MarianDCrafter
 */
public class Main extends Plugin {

    private static Main instance;
    private static Configuration configuration;
    private static GameManager gameManager;

    /**
     * Called when the plugin is enabled. Sets up everything including listeners, commands etc.
     */
    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
        loadGameManager();

        System.out.println("2. Devathlon, 3. Runde - GreenGlowPixel-Team - Plugin enabled!");
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

    @Override
    protected String prefix() {
        return "DevAthlon";
    }

    @Override
    protected ChatColor defaultColor() {
        return ChatColor.GOLD;
    }

}
