package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Location;

import java.util.List;

/**
 * A map for the game.
 */
public class GameMap {

    private String name;
    private String creator;
    private Location spawn, catcherSpawn;
    private Area validArrowArea;
    private List<Base> bases;
    private List<Location> hoppers;

    /**
     * Creates a new map.
     * @param name the name of the map
     * @param creator the creator(s) of the map
     * @param spawn the spawn point of the map
     * @param catcherSpawn the catcher spawn point of the map
     * @param validArrowArea the area where the arrows are allowed to land
     * @param bases the bases of the map
     * @param hoppers the hopper locations of the map
     */
    public GameMap(String name, String creator, Location spawn, Location catcherSpawn,
                   Area validArrowArea, List<Base> bases, List<Location> hoppers) {
        this.name = name;
        this.creator = creator;
        this.spawn = spawn;
        this.catcherSpawn = catcherSpawn;
        this.validArrowArea = validArrowArea;
        this.bases = bases;
        this.hoppers = hoppers;
    }

    /**
     * @return the name of the map
     */
    public String getName() {
        return name;
    }

    /**
     * @return the creator of the map
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @return the spawn point of the map
     */
    public Location getSpawn() {
        return spawn;
    }

    /**
     * @return the catcher spawn point of the map
     */
    public Location getCatcherSpawn() {
        return catcherSpawn;
    }

    /**
     * @return the area where the arrows are allowed to land
     */
    public Area getValidArrowArea() {
        return validArrowArea;
    }

    /**
     * @return the bases of the map
     */
    public List<Base> getBases() {
        return bases;
    }

    /**
     * @return the hopper locations of the map
     */
    public List<Location> getHoppers() {
        return hoppers;
    }

    /**
     * Closes the gates of all bases. Used at the start of every game to make sure every gate is closed.
     */
    public void closeAllGates() {
        for (Base base : bases) {
            base.closeEntrance();
            base.closeExit();
        }
    }

}
