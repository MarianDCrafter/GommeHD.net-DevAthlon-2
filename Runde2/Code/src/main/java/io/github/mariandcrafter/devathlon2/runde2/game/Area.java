package io.github.mariandcrafter.devathlon2.runde2.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * A cuboid area represented by two points in a world.
 * Copied from round one of the DevAthlon.
 */
public class Area {

    private World world;
    private int startX, startY, startZ;
    private int endX, endY, endZ;

    /**
     * Creates a new area with the two points
     *
     * @param start point 1
     * @param end   point 2
     */
    public Area(Location start, Location end) {
        this.world = start.getWorld();

        this.startX = start.getBlockX();
        this.startY = start.getBlockY();
        this.startZ = start.getBlockZ();

        this.endX = end.getBlockX();
        this.endY = end.getBlockY();
        this.endZ = end.getBlockZ();
    }

    /**
     * Checks whether the given location is in this area. The block coordinates of the location are used.
     *
     * @param location the location to check
     * @return {@code true} if the location is contained in this area, otherwise {@code false}
     */
    public boolean containsBlockLocation(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return ((startX <= x && x <= endX) || (endX <= x && x <= startX)) &&
                ((startY <= y && y <= endY) || (endY <= y && y <= startY)) &&
                ((startZ <= z && z <= endZ) || (endZ <= z && z <= startZ));
    }

    /**
     * Fills this area with the given material.
     *
     * @param material the material to fill with
     */
    public void fill(Material material) {

        // The min/max points have to be used, so we can start in the for-loop with the lower coordinate and end with
        // the higher:

        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);

        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        int minZ = Math.min(startZ, endZ);
        int maxZ = Math.max(startZ, endZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // set every block in the area to the given material
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }

}
