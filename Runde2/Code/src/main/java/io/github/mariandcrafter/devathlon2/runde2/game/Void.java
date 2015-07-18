package io.github.mariandcrafter.devathlon2.runde2.game;

import org.bukkit.Location;

/**
 * The void of the map where the players can be sucked out.
 */
public class Void {

    /**
     * The area where the players are sucked out.
     */
    private Area area;
    /**
     * The location where the players should move to.
     */
    private Location toLocation;

    /**
     * Creates a new void
     * @param area the area of the void
     * @param toLocation the location where the players should move to
     */
    public Void(Area area, Location toLocation) {
        this.area = area;
        this.toLocation = toLocation;
    }

    /**
     * @return the area where the players area sucked out
     */
    public Area getArea() {
        return area;
    }

    /**
     * @return the location where the players should move to
     */
    public Location getToLocation() {
        return toLocation;
    }

}
