package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Location;

import java.util.List;

public class GameMap {

    private String name;
    private String creator;
    private Location spawn, catcherSpawn;
    private Area validArrowArea;
    private List<Base> bases;
    private List<Location> hoppers;

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

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Location getCatcherSpawn() {
        return catcherSpawn;
    }

    public Area getValidArrowArea() {
        return validArrowArea;
    }

    public List<Base> getBases() {
        return bases;
    }

    public List<Location> getHoppers() {
        return hoppers;
    }

    public void closeAllGates() {
        for (Base base : bases) {
            base.closeEntrance();
            base.closeExit();
        }
    }

}
