package io.github.mariandcrafter.devathlon2.runde3;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the DevAthlon plugin.
 * @author MarianDCrafter
 */
public class Main extends JavaPlugin {

    private static Main instance;

    /**
     * Called when the plugin is enabled. Sets up everything including listeners, commands etc.
     */
    @Override
    public void onEnable() {
        instance = this;

        System.out.println("2. Devathlon, 3. Runde - GreenGlowPixel-Team - Plugin enabled!");
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

}