package io.github.mariandcrafter.devathlon2.runde2.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A map for the game.
 */
public class GameMap {

    private String name;
    private String creator;
    private Location catcherSpawn;
    private List<Location> runnerSpawns;
    private List<Location> armorStandLocations;
    private List<ArmorStand> armorStands = new ArrayList<ArmorStand>();

    /**
     * Creates a new map
     * @param name
     * @param creator
     * @param catcherSpawn
     * @param runnerSpawns
     * @param armorStandLocations
     */
    public GameMap(String name, String creator, Location catcherSpawn, List<Location> runnerSpawns,
                   List<Location> armorStandLocations) {
        this.name = name;
        this.creator = creator;
        this.catcherSpawn = catcherSpawn;
        this.runnerSpawns = runnerSpawns;
        this.armorStandLocations = armorStandLocations;
    }

    /**
     * @return the name of the map
     */
    public String getName() {
        return name;
    }

    /**
     * @return the creator of the map
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @return the catcher spawn point of the map
     */
    public Location getCatcherSpawn() {
        return catcherSpawn;
    }

    /**
     * @return the runner spawn points of the map
     */
    public List<Location> getRunnerSpawns() {
        return runnerSpawns;
    }

    /**
     * @return the armor stand locations of the map
     */
    public List<Location> getArmorStandLocations() {
        return armorStandLocations;
    }

    /**
     * Puts the armor into the armor stands.
     */
    public void fillArmorStands() {

        // remove current armor stands:
        for (ArmorStand armorStand : armorStands) {
            armorStand.remove();
        }
        armorStands.clear();

        // spawn the armor stands:
        armorStands.add(spawnArmorStand(armorStandLocations.get(0)));
        armorStands.add(spawnArmorStand(armorStandLocations.get(1)));
        armorStands.add(spawnArmorStand(armorStandLocations.get(2)));
        armorStands.add(spawnArmorStand(armorStandLocations.get(3)));

        // put the armor into the armor stands:
        armorStands.get(0).setHelmet(new ItemStack(Material.IRON_HELMET));
        armorStands.get(1).setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        armorStands.get(2).setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        armorStands.get(3).setBoots(new ItemStack(Material.IRON_BOOTS));
    }

    private ArmorStand spawnArmorStand(Location location) {
        return location.getWorld().spawn(location, ArmorStand.class);
    }

}
