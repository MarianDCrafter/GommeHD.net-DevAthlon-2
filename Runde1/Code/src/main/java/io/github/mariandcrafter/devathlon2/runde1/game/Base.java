package io.github.mariandcrafter.devathlon2.runde1.game;

import org.bukkit.Material;

public class Base {

    private Material gateMaterial;
    private Area area;
    private Area entrance, exit;

    public Base(Material gateMaterial, Area area, Area entrance, Area exit) {
        System.out.println(gateMaterial);
        this.gateMaterial = gateMaterial;
        this.area = area;
        this.entrance = entrance;
        this.exit = exit;
    }

    public Area getArea() {
        return area;
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
