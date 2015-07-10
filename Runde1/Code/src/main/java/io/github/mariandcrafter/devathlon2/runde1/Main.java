package io.github.mariandcrafter.devathlon2.runde1;

import io.github.mariandcrafter.devathlon2.runde1.commands.StartGameCommands;
import io.github.mariandcrafter.devathlon2.runde1.listeners.JoinListener;
import io.github.mariandcrafter.devathlon2.runde1.listeners.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {

    private static Main instance;
    private static Configuration configuration;
    private static PlayerManager playerManager;

    private List<Listener> listeners;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
        loadPlayerManager();
        loadListeners();
        loadCommands();

        System.out.println("2. Devathlon, 1. Runde - GreenGlowPixel-Team - Plugin enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("2. Devathlon, 1. Runde - GreenGlowPixel-Team - Plugin disabled!");
    }

    public void loadConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        configuration = new Configuration(getConfig());
    }

    public void loadPlayerManager() {
        playerManager = new PlayerManager();

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerManager.onJoin(player);
        }
    }

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
    }

    public void loadCommands() {
        StartGameCommands startGameCommands = new StartGameCommands();
        getCommand("invite").setExecutor(startGameCommands);
        getCommand("accept").setExecutor(startGameCommands);
        getCommand("deny").setExecutor(startGameCommands);
    }

    public static Main getInstance() {
        return instance;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static PlayerManager getPlayerManager() {
        return playerManager;
    }

}
