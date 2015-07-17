package io.github.mariandcrafter.devathlon2.runde2.game;

import org.bukkit.Location;

public class Void {

    private Area area;
    private Location toLocation;

    public Void(Area area, Location toLocation) {
        this.area = area;
        this.toLocation = toLocation;
    }

    public Area getArea() {
        return area;
    }

    public Location getToLocation() {
        return toLocation;
    }

}
