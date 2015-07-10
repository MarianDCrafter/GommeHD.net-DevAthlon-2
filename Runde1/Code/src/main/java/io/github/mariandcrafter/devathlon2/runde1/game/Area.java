package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Location;

public class Area {

    private Location start, end;

    public Area(Location start, Location end) {
        this.start = start;
        this.end = end;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

}
