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

public class Configuration {

    private FileConfiguration configuration;

    private Location spawn;
    private List<GameMap> gameMaps;

    public Configuration(FileConfiguration configuration) {
        this.configuration = configuration;
        loadConfiguration();
    }

    private void loadConfiguration() {
        spawn = loadLocationWithYawAndPitch("lobbySpawn");
        gameMaps = loadMaps("maps");
    }

    private List<GameMap> loadMaps(String path) {
        List<GameMap> gameMaps = new ArrayList<GameMap>();
        for (String mapPath : configuration.getConfigurationSection(path).getKeys(false)) {
            gameMaps.add(loadMap(path + "." + mapPath));
        }
        return gameMaps;
    }

    private GameMap loadMap(String path) {
        World world = Bukkit.getWorld(configuration.getString(path + ".world"));
        return new GameMap(
                configuration.getString(path + ".name"),
                configuration.getString(path + ".creator"),
                loadLocationWithYawAndPitch(world, path + ".spawn"),
                loadLocationWithYawAndPitch(world, path + ".runnerSpawn"),
                loadLocationWithYawAndPitch(world, path + ".catcherSpawn"),
                loadArea(world, path + ".validArrowArea"),
                loadBases(world, path + ".bases"),
                loadHoppers(world, path + ".hoppers")
        );
    }

    private List<Base> loadBases(World world, String path) {
        List<Base> bases = new ArrayList<Base>();
        for (String basePath : configuration.getConfigurationSection(path).getKeys(false)) {
            bases.add(loadBase(world, path + "." + basePath));
        }
        return bases;
    }

    private Base loadBase(World world, String path) {
        return new Base(
                Material.getMaterial(configuration.getString(path + ".gateMaterial")),
                loadArea(world, path + ".area"),
                loadArea(world, path + ".entranceArea"),
                loadArea(world, path + ".exitArea")
        );
    }

    private List<Location> loadHoppers(World world, String path) {
        List<Location> hoppers = new ArrayList<Location>();
        for (String hopperPath : configuration.getConfigurationSection(path).getKeys(false)) {
            hoppers.add(loadBlockLocation(world, path + "." + hopperPath));
        }
        return hoppers;
    }

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

    private Location loadBlockLocation(World world, String path) {
        return new Location(
                world,
                configuration.getDouble(path + ".x"),
                configuration.getDouble(path + ".y"),
                configuration.getDouble(path + ".z")
        );
    }

    private Area loadArea(World world, String path) {
        return new Area(
                loadBlockLocation(world, path + ".start"),
                loadBlockLocation(world, path + ".end")
        );
    }

    public Location getSpawn() {
        return spawn;
    }

    public List<GameMap> getGameMaps() {
        return gameMaps;
    }

}
