package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Material;

public class Base {

    private Material gateMaterial;
    private Area area;
    private Area entrance, exit;

    public Base(Material gateMaterial, Area area, Area entrance, Area exit) {
        this.gateMaterial = gateMaterial;
        this.area = area;
        this.entrance = entrance;
        this.exit = exit;
    }

}
