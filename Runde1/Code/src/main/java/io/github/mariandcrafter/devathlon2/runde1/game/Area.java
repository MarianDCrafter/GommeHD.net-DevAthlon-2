package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Area {

    private World world;
    private int startX, startY, startZ;
    private int endX, endY, endZ;

    public Area(Location start, Location end) {
        this.world = start.getWorld();

        this.startX = start.getBlockX();
        this.startY = start.getBlockY();
        this.startZ = start.getBlockZ();

        this.endX = end.getBlockX();
        this.endY = end.getBlockY();
        this.endZ = end.getBlockZ();
    }

    public boolean containsBlockLocation(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return ((startX <= x && x <= endX) || (endX <= x && x <= startX)) &&
               ((startY <= y && y <= endY) || (endY <= y && y <= startY)) &&
               ((startZ <= z && z <= endZ) || (endZ <= z && z <= startZ));
    }

    public void fill(Material material) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);

        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        int minZ = Math.min(startZ, endZ);
        int maxZ = Math.max(startZ, endZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }

}
