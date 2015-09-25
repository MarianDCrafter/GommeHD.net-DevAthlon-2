package io.github.mariandcrafter.devathlon2.runde3;

import io.github.mariandcrafter.devathlon2.runde3.game.VillagerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    private FileConfiguration configuration;

    private Location lobbySpawn;
    private Location mapSpawn;
    private List<Location> silverfishSpawns;
    private Map<Location, VillagerType> villagerSpawns;

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
        World world = Bukkit.getWorld(configuration.getString("world"));

        lobbySpawn = loadLocationWithYawAndPitch(world, "lobbySpawn");
        mapSpawn = loadLocationWithYawAndPitch(world, "mapSpawn");
        silverfishSpawns = loadSilverfishSpawns(world, "silverfishSpawns");
        villagerSpawns = loadVillagerSpawns(world, "villagerSpawns");
    }

    private List<Location> loadSilverfishSpawns(World world, String path) {
        List<Location> silverfishSpawns = new ArrayList<Location>();
        for (String silverfishSpawnPath : configuration.getConfigurationSection(path).getKeys(false)) {
            silverfishSpawns.add(loadBlockLocation(world, path + "." + silverfishSpawnPath));
        }
        return silverfishSpawns;
    }

    private Map<Location, VillagerType> loadVillagerSpawns(World world, String path) {
        Map<Location, VillagerType> villagerSpawns = new HashMap<Location, VillagerType>();
        villagerSpawns.put(loadLocationWithYawAndPitch(world, path + ".archery"), VillagerType.ARCHERY);
        return villagerSpawns;
    }

    /**
     * Loads a location with world, x, y, z, yaw and pitch.
     *
     * @param path the path to the location section
     * @return the loaded location
     */
    private Location loadLocation(World world, String path) {
        return new Location(
                world,
                configuration.getDouble(path + ".x"),
                configuration.getDouble(path + ".y"),
                configuration.getDouble(path + ".z")
        );
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

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public Location getMapSpawn() {
        return mapSpawn;
    }

    public List<Location> getSilverfishSpawns() {
        return silverfishSpawns;
    }

    public Map<Location, VillagerType> getVillagerSpawns() {
        return villagerSpawns;
    }
}
