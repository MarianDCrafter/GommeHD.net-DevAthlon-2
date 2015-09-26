package io.github.mariandcrafter.devathlon2.runde3;

import io.github.mariandcrafter.devathlon2.runde3.game.Area;
import io.github.mariandcrafter.devathlon2.runde3.game.VillagerType;
import io.github.mariandcrafter.devathlon2.runde3.game.archery.Archery;
import io.github.mariandcrafter.devathlon2.runde3.game.swordplay.Swordplay;
import io.github.mariandcrafter.devathlon2.runde3.game.witchhunt.Witchhunt;
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
    private Archery archery;
    private Swordplay swordplay;
    private Witchhunt witchhunt;

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
        archery = loadArchery(world, "gamemodes.archery");
        swordplay = loadSwordplay(world, "gamemodes.swordplay");
        witchhunt = loadWitchhunt(world, "gamemodes.witchHunt");
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
        villagerSpawns.put(loadLocationWithYawAndPitch(world, path + ".witchhunt"), VillagerType.WITCHHUNT);
        for (String villagerSpawnPath : configuration.getConfigurationSection(path + ".healersCheap").getKeys(false)) {
            villagerSpawns.put(loadLocation(world, path + ".healersCheap." + villagerSpawnPath), VillagerType.HEALER_CHEAP);
        }
        for (String villagerSpawnPath : configuration.getConfigurationSection(path + ".healersExpensive").getKeys(false)) {
            villagerSpawns.put(loadLocation(world, path + ".healersExpensive." + villagerSpawnPath), VillagerType.HEALER_EXPENSIVE);
        }
        return villagerSpawns;
    }

    private Archery loadArchery(World world, String path) {
        Area area = loadArea(world, path + ".area");
        List<Location> hayBlocks = new ArrayList<Location>();
        for (String hayBlockPath : configuration.getConfigurationSection(path + ".hayBlocks").getKeys(false)) {
            hayBlocks.add(loadBlockLocation(world, path + ".hayBlocks." + hayBlockPath));
        }
        return new Archery(area, hayBlocks);
    }

    private Swordplay loadSwordplay(World world, String path) {
        Location plate1 = loadBlockLocation(world, path + ".plate1");
        Location plate2 = loadBlockLocation(world, path + ".plate2");
        Location spawn1 = loadLocation(world, path + ".spawn1");
        Location spawn2 = loadLocation(world, path + ".spawn2");
        Location spawnAfter = loadLocation(world, path + ".spawnAfter");
        return new Swordplay(plate1, plate2, spawn1, spawn2, spawnAfter);
    }

    private Witchhunt loadWitchhunt(World world, String path) {
        Location spawn = loadLocation(world, path + ".spawn");
        Location spawnAfter = loadLocation(world, path + ".spawnAfter");
        Location witchSpawn = loadLocation(world, path + ".witchSpawn");
        List<Location> skeletonSpawns = new ArrayList<Location>(), spiderSpawns = new ArrayList<Location>(), zombieSpawns = new ArrayList<Location>();

        for (String skeletonSpawnPath : configuration.getConfigurationSection(path + ".skeletonSpawns").getKeys(false))
            skeletonSpawns.add(loadLocation(world, path + ".skeletonSpawns." + skeletonSpawnPath));
        for (String spiderSpawnPath : configuration.getConfigurationSection(path + ".spiderSpawns").getKeys(false))
            spiderSpawns.add(loadLocation(world, path + ".spiderSpawns." + spiderSpawnPath));
        for (String zombieSpawnPath : configuration.getConfigurationSection(path + ".zombieSpawns").getKeys(false))
            skeletonSpawns.add(loadLocation(world, path + ".zombieSpawns." + zombieSpawnPath));

        return new Witchhunt(spawn, spawnAfter, witchSpawn, skeletonSpawns, spiderSpawns, zombieSpawns);
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

    public Archery getArchery() {
        return archery;
    }

    public Swordplay getSwordplay() {
        return swordplay;
    }

    public Witchhunt getWitchhunt() {
        return witchhunt;
    }

}
