package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Location;
import org.bukkit.Material;

public class Base {

    private Material gateMaterial;
    private Location spawn;
    private Area area;
    private Area entrance, exit;

    public Base(Material gateMaterial, Location spawn, Area area, Area entrance, Area exit) {
        this.gateMaterial = gateMaterial;
        this.spawn = spawn;
        this.area = area;
        this.entrance = entrance;
        this.exit = exit;
    }

    public Area getArea() {
        return area;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void openEntrance() {
        entrance.fill(Material.AIR);
    }

    public void closeEntrance() {
        entrance.fill(gateMaterial);
    }

    public void openExit() {
        exit.fill(Material.AIR);
    }

    public void closeExit() {
        exit.fill(gateMaterial);
    }

}
