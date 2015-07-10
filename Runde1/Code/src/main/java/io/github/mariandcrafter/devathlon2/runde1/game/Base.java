package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * A base or a 'checkpoint' of the game.
 */
public class Base {

    private Material gateMaterial;
    private Location spawn;
    private Area area;
    private Area entrance, exit;

    /**
     * Creates a new base.
     *
     * @param gateMaterial the material for the gates (entrance and exit)
     * @param spawn        the spawn point for the runner in this base
     * @param area         the area of this base
     * @param entrance     the entrance area of this base that can be filled with the given material
     * @param exit         the exit area of this base that can be filled with the given material
     */
    public Base(Material gateMaterial, Location spawn, Area area, Area entrance, Area exit) {
        this.gateMaterial = gateMaterial;
        this.spawn = spawn;
        this.area = area;
        this.entrance = entrance;
        this.exit = exit;
    }

    /**
     * @return the area of this base
     */
    public Area getArea() {
        return area;
    }

    /**
     * @return the spawn point for the runner of this base
     */
    public Location getSpawn() {
        return spawn;
    }

    /**
     * Opens (or fills with air) the entrance area of this base.
     */
    public void openEntrance() {
        entrance.fill(Material.AIR);
    }

    /**
     * Closes (or fills with the gate material) the entrance area of this base.
     */
    public void closeEntrance() {
        entrance.fill(gateMaterial);
    }

    /**
     * Opens (or fills with air) the exit area of this base.
     */
    public void openExit() {
        exit.fill(Material.AIR);
    }

    /**
     * Closes (or fills with the gate material) the exit area of this base.
     */
    public void closeExit() {
        exit.fill(gateMaterial);
    }

}
