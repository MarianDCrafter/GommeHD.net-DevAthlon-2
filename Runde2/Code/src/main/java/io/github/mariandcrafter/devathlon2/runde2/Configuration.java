package io.github.mariandcrafter.devathlon2.runde2;

import io.github.mariandcrafter.devathlon2.runde2.game.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * This class takes the configuration file in the constructor and loads the data from it.
 */
public class Configuration {

    private FileConfiguration configuration;

    private Location spawn;
    private List<GameMap> gameMaps;

    /**
     * @param configuration the configuration file of the plugin
     */
    public Configuration(FileConfiguration configuration) {
        this.configuration = configuration;

        loadConfiguration();
    }

    /**
     * Loads the data from the file.
     */
    private void loadConfiguration() {
        spawn = loadLocationWithYawAndPitch("lobbySpawn");
        gameMaps = loadMaps("maps");
    }

    /**
     * Loads the maps from the file.
     *
     * @param path the path to the maps section
     * @return a list with all loaded maps
     */
    private List<GameMap> loadMaps(String path) {
        List<GameMap> gameMaps = new ArrayList<GameMap>();
        for (String mapPath : configuration.getConfigurationSection(path).getKeys(false)) {
            gameMaps.add(loadMap(path + "." + mapPath));
            System.out.println("load map " + mapPath);
        }
        return gameMaps;
    }

    /**
     * Loads a map from the file.
     *
     * @param path the path to the map section
     * @return the loaded map
     */
    private GameMap loadMap(String path) {
        World world = Bukkit.getWorld(configuration.getString(path + ".world"));
        return new GameMap(
                configuration.getString(path + ".name"),
                configuration.getString(path + ".creator"),
                loadLocationWithYawAndPitch(world, path + ".catcherSpawn"),
                loadRunnerSpawns(world, path + ".runnerSpawns"),
                loadArmorStands(world, path + ".armorStands")
        );
    }

    /**
     * Loads all runner spawns of a map from the file.
     *
     * @param world the world of the map
     * @param path  the path to the runner spawns section
     * @return a list with all loaded runner spawns
     */
    private List<Location> loadRunnerSpawns(World world, String path) {
        List<Location> runnerSpawns = new ArrayList<Location>();
        for (String runnerSpawnsPath : configuration.getConfigurationSection(path).getKeys(false)) {
            runnerSpawns.add(loadLocationWithYawAndPitch(world, path + "." + runnerSpawnsPath));
        }
        return runnerSpawns;
    }

    /**
     * Loads all armor stand locations of a map from the file.
     *
     * @param world the world of the map
     * @param path  the path to the armor stands section
     * @return a list with all loaded armor stand locations
     */
    private List<Location> loadArmorStands(World world, String path) {
        List<Location> armorStands = new ArrayList<Location>();
        for (String armorStandPath : configuration.getConfigurationSection(path).getKeys(false)) {
            armorStands.add(loadBlockLocation(world, path + "." + armorStandPath));
        }
        return armorStands;
    }

    /**
     * Loads a location with world, x, y, z, yaw and pitch.
     *
     * @param path the path to the location section
     * @return the loaded location
     */
    private Location loadLocationWithYawAndPitch(String path) {
        return new Location(
                Bukkit.getWorld(configuration.getString(path + ".world")),
                configuration.getDouble(path + ".x"),
                configuration.getDouble(path + ".y"),
                configuration.getDouble(path + ".z"),
                (float) configuration.getDouble(path + ".yaw"),
                (float) configuration.getDouble(path + ".pitch")
        );
    }

    /**
     * Loads a location with x, y, z, yaw and pitch.
     *
     * @param world the world of the location
     * @param path  the path to the location section
     * @return the loaded location
     */
    private Location loadLocationWithYawAndPitch(World world, String path) {
        return new Location(
                world,
                configuration.getDouble(path + ".x"),
                configuration.getDouble(path + ".y"),
                configuration.getDouble(path + ".z"),
                (float) configuration.getDouble(path + ".yaw"),
                (float) configuration.getDouble(path + ".pitch")
        );
    }

    /**
     * Loads a location with x, y and z.
     *
     * @param world the world of the location
     * @param path  the path to the location section
     * @return the loaded location
     */
    private Location loadBlockLocation(World world, String path) {
        return new Location(
                world,
                configuration.getDouble(path + ".x"),
                configuration.getDouble(path + ".y"),
                configuration.getDouble(path + ".z")
        );
    }

    /**
     * @return the spawn of the lobby
     */
    public Location getSpawn() {
        return spawn;
    }

    /**
     * @return a list with all maps
     */
    public List<GameMap> getGameMaps() {
        return gameMaps;
    }

}