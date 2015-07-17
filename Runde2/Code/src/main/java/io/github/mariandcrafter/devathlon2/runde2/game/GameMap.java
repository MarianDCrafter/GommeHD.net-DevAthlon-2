package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
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
    private List<Location> freeArmorStandLocations;
    private List<ArmorStand> armorStands = new ArrayList<ArmorStand>();
    private RescueCapsule rescueCapsule;

    /**
     * Creates a new map
     * @param name
     * @param creator
     * @param catcherSpawn
     * @param runnerSpawns
     * @param armorStandLocations
     */
    public GameMap(String name, String creator, Location catcherSpawn, List<Location> runnerSpawns,
                   List<Location> armorStandLocations, RescueCapsule rescueCapsule) {
        this.name = name;
        this.creator = creator;
        this.catcherSpawn = catcherSpawn;
        this.runnerSpawns = runnerSpawns;
        this.armorStandLocations = armorStandLocations;
        this.rescueCapsule = rescueCapsule;
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
     * @return the current used armor stands
     */
    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    /**
     * @return the rescue capsule of the map
     */
    public RescueCapsule getRescueCapsule() {
        return rescueCapsule;
    }

    /**
     * Puts the armor into the armor stands.
     */
    public void fillArmorStands() {
        freeArmorStandLocations = new ArrayList<Location>(armorStandLocations);

        // remove current armor stands:
        for (ArmorStand armorStand : armorStands) {
            armorStand.remove();
        }
        armorStands.clear();

        // spawn the armor stands:
        for (int i = 0; i < 4; i++) {
            armorStands.add(spawnArmorStand());
        }

        // put the armor into the armor stands:
        armorStands.get(0).setHelmet(new ItemStack(Material.IRON_HELMET));
        armorStands.get(1).setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        armorStands.get(2).setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        armorStands.get(3).setBoots(new ItemStack(Material.IRON_BOOTS));
    }

    private ArmorStand spawnArmorStand() {
        Location location = freeArmorStandLocations.get(Main.getRandom().nextInt(freeArmorStandLocations.size()));
        freeArmorStandLocations.remove(location);
        return location.getWorld().spawn(location, ArmorStand.class);
    }

    public void removeArmorStand(ArmorStand armorStand) {
        freeArmorStandLocations.add(armorStand.getLocation());
        armorStand.remove();
        armorStands.remove(armorStand);
    }

    public void teleportArmorStand(ArmorStand armorStand, ItemStack itemStack) {

        // remove the old armor stand:
        Location oldLocation = armorStand.getLocation();
        armorStand.remove();
        armorStands.remove(armorStand);

        // spawn a new armor stand:
        armorStand = spawnArmorStand();
        armorStands.add(armorStand);

        switch (itemStack.getType()) {
            case IRON_HELMET:
                armorStand.setHelmet(itemStack);
                break;
            case IRON_CHESTPLATE:
                armorStand.setChestplate(itemStack);
                break;
            case IRON_LEGGINGS:
                armorStand.setLeggings(itemStack);
                break;
            case IRON_BOOTS:
                armorStand.setBoots(itemStack);
                break;
        }

        // insert the old location into the list with the free armor locations
        freeArmorStandLocations.add(oldLocation);

    }

}
