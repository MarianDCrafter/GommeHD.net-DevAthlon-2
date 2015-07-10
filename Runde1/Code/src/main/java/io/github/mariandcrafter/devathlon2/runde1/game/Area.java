package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Location;

public class Area {

    private int startX, startY, startZ;
    private int endX, endY, endZ;

    public Area(Location start, Location end) {
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

}
