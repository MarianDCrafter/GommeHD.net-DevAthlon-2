package io.github.mariandcrafter.devathlon2.runde2.game;

import org.bukkit.Location;
import org.bukkit.Material;

public class RescueCapsule {

    private Material entranceMaterial, exitMaterial;
    private Area entranceArea, exitArea;
    private Location rescueButtonLocation;

    public RescueCapsule(Material entranceMaterial, Area entranceArea, Material exitMaterial, Area exitArea, Location rescueButtonLocation) {
        this.entranceMaterial = entranceMaterial;
        this.entranceArea = entranceArea;
        this.exitMaterial = exitMaterial;
        this.exitArea = exitArea;
        this.rescueButtonLocation = rescueButtonLocation;
    }

    public void openEntrance() {
        entranceArea.fill(Material.AIR);
    }

    public void closeEntrance() {
        entranceArea.fill(entranceMaterial);
    }

    public void openExit() {
        exitArea.fill(Material.AIR);
    }

    public void closeExit() {
        exitArea.fill(exitMaterial);
    }

    public Location getRescueButtonLocation() {
        return rescueButtonLocation;
    }
}
