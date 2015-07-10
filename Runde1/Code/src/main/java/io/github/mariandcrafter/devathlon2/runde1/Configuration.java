package io.github.mariandcrafter.devathlon2.runde1;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {

    private FileConfiguration configuration;

    private Location spawn;

    public Configuration(FileConfiguration configuration) {
        this.configuration = configuration;
        loadConfiguration();
    }

    private void loadConfiguration() {
        spawn = loadLocationWithYawAndPitch("lobbySpawn");
    }

    private Location loadLocationWithYawAndPitch(String path) {
        System.out.println(Bukkit.getWorld(path + ".world"));
        System.out.println(configuration.getDouble(path + ".x"));
        System.out.println(configuration.getDouble(path + ".y"));
        System.out.println(configuration.getDouble(path + ".z"));
        System.out.println(configuration.getDouble(path + ".yaw"));
        System.out.println(configuration.getDouble(path + ".pitch"));
        return new Location(
                Bukkit.getWorld(configuration.getString(path + ".world")),
                configuration.getDouble(path + ".x"),
                configuration.getDouble(path + ".y"),
                configuration.getDouble(path + ".z"),
                (float) configuration.getDouble(path + ".yaw"),
                (float) configuration.getDouble(path + ".pitch")
        );
    }

    public Location getSpawn() {
        return spawn;
    }

}
