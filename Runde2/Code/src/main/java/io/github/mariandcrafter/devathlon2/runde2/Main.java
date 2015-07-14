package io.github.mariandcrafter.devathlon2.runde2;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    /**
     * @return the instance of the main class
     */
    public static Main getInstance() {
        return instance;
    }

}
