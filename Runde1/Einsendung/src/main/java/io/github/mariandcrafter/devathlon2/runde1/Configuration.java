package io.github.mariandcrafter.devathlon2.runde1;

import io.github.mariandcrafter.devathlon2.runde1.game.Area;
import io.github.mariandcrafter.devathlon2.runde1.game.Base;
import io.github.mariandcrafter.devathlon2.runde1.game.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private Database database;
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
        database = loadDatabase("database");
        gameMaps = loadMaps("maps");
    }

    /**
     * Loads the database data from the file.
     *
     * @param path the path to the database section
     * @return the database
     */
    private Database loadDatabase(String path) {
        return new Database(
                configuration.getString(path + ".host"),
                configuration.getString(path + ".port"),
                configuration.getString(path + ".database"),
                configuration.getString(path + ".user"),
                configuration.getString(path + ".password")
        );
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
                loadLocationWithYawAndPitch(world, path + ".spawn"),
                loadLocationWithYawAndPitch(world, path + ".catcherSpawn"),
                loadArea(world, path + ".validArrowArea"),
                loadBases(world, path + ".bases"),
                loadHoppers(world, path + ".hoppers")
        );
    }

    /**
     * Loads all bases of a map from the file.
     *
     * @param world the world of the map
     * @param path  the path to the bases section
     * @return a list with all loaded bases
     */
    private List<Base> loadBases(World world, String path) {
        List<Base> bases = new ArrayList<Base>();
        for (String basePath : configuration.getConfigurationSection(path).getKeys(false)) {
            bases.add(loadBase(world, path + "." + basePath));
        }
        return bases;
    }

    /**
     * Loads a base from the file.
     *
     * @param world the world of the map
     * @param path  the path to the base section
     * @return the loaded base
     */
    private Base loadBase(World world, String path) {
        return new Base(
                Material.getMaterial(configuration.getString(path + ".gateMaterial")),
                loadLocationWithYawAndPitch(world, path + ".spawn"),
                loadArea(world, path + ".area"),
                loadArea(world, path + ".entranceArea"),
                loadArea(world, path + ".exitArea")
        );
    }

    /**
     * Loads all hopper locations of a map from the file.
     *
     * @param world the world of the map
     * @param path  the path to the hoppers section
     * @return a list with all loaded hopper locations
     */
    private List<Location> loadHoppers(World world, String path) {
        List<Location> hoppers = new ArrayList<Location>();
        for (String hopperPath : configuration.getConfigurationSection(path).getKeys(false)) {
            hoppers.add(loadBlockLocation(world, path + "." + hopperPath));
        }
        return hoppers;
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
     * Loads an area with a start and an end location.
     *
     * @param world the world of the area
     * @param path  the path to the area section
     * @return the loaded area
     */
    private Area loadArea(World world, String path) {
        return new Area(
                loadBlockLocation(world, path + ".start"),
                loadBlockLocation(world, path + ".end")
        );
    }

    /**
     * @return the spawn of the lobby
     */
    public Location getSpawn() {
        return spawn;
    }

    /**
     * @return the database
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * @return a list with all maps
     */
    public List<GameMap> getGameMaps() {
        return gameMaps;
    }

}
