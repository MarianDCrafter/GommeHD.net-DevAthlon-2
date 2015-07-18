package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A map for the game.
 */
public class GameMap {

    private String name;
    private String creator;
    private Location catcherSpawn;
    private Location runnerSpawn;
    private List<Location> armorStandLocations;
    private List<Location> freeArmorStandLocations;
    private List<ArmorStand> armorStands = new ArrayList<ArmorStand>();
    private RescueCapsule rescueCapsule;
    private Void mapVoid;
    private List<Location> teleportationPoints;

    /**
     * Creates a new map.
     *
     * @param name                the name of the map
     * @param creator             the creator of the map
     * @param catcherSpawn        the spawn of the catcher
     * @param runnerSpawn         the spawn of the runner
     * @param armorStandLocations the possible locations of the armor stands
     * @param rescueCapsule       the rescue capsule of the map
     * @param mapVoid             the void of the map
     * @param teleportationPoints the possible teleportation points of the map
     */
    public GameMap(String name, String creator, Location catcherSpawn, Location runnerSpawn,
                   List<Location> armorStandLocations, RescueCapsule rescueCapsule, Void mapVoid, List<Location> teleportationPoints) {
        this.name = name;
        this.creator = creator;
        this.catcherSpawn = catcherSpawn;
        this.runnerSpawn = runnerSpawn;
        this.armorStandLocations = armorStandLocations;
        this.rescueCapsule = rescueCapsule;
        this.mapVoid = mapVoid;
        this.teleportationPoints = teleportationPoints;
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
     * @return the runner spawn point of the map
     */
    public Location getRunnerSpawn() {
        return runnerSpawn;
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
     * @return the void of the map
     */
    public Void getMapVoid() {
        return mapVoid;
    }

    /**
     * @return the possible teleportation points of the map
     */
    public List<Location> getTeleportationPoints() {
        return teleportationPoints;
    }

    /**
     * Spawns four armor stands and puts the armor into them.
     */
    public void spawnArmorStands() {
        // All armor stand locations are now free:
        freeArmorStandLocations = new ArrayList<Location>(armorStandLocations);

        // Remove current armor stands:
        for (ArmorStand armorStand : armorStands) {
            armorStand.remove();
        }
        armorStands.clear();

        // Spawn the armor stands:
        for (int i = 0; i < 4; i++)
            armorStands.add(spawnArmorStand());

        // Put the armor into the armor stands:
        armorStands.get(0).setHelmet(new ItemStack(Material.IRON_HELMET));
        armorStands.get(1).setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        armorStands.get(2).setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        armorStands.get(3).setBoots(new ItemStack(Material.IRON_BOOTS));
    }

    /**
     * Spawns an armor stand at a random free armor stand location.
     *
     * @return the spawned armor stand
     */
    private ArmorStand spawnArmorStand() {
        Location location = freeArmorStandLocations.get(Main.getRandom().nextInt(freeArmorStandLocations.size()));
        freeArmorStandLocations.remove(location);
        return location.getWorld().spawn(location, ArmorStand.class);
    }

    /**
     * Removes the given armor stand. Also plays a flame effect at the location of the armor stand.
     *
     * @param armorStand the armor stand to remove
     */
    public void removeArmorStand(ArmorStand armorStand) {
        Random random = Main.getRandom();
        for (int i = 0; i < 10; i++) {
            Location location = armorStand.getLocation().add(random.nextDouble() * 0.5, random.nextDouble() * 0.5 + 1, random.nextDouble() * 0.5);
            PlayerUtils.playEffect(location, Effect.HAPPY_VILLAGER, null);
        }

        // Add the location to the free armor stand locations and remove the armor stand from the armor stand list:
        freeArmorStandLocations.add(armorStand.getLocation());
        armorStand.remove();
        armorStands.remove(armorStand);
    }

    /**
     * Teleports the given armor stand to a random free armor stand location.
     *
     * @param armorStand the armor stand to teleport
     * @param itemStack  the item stack to put into the new armor stand
     */
    public void teleportArmorStand(ArmorStand armorStand, ItemStack itemStack) {

        // Remove the old armor stand:
        Location oldLocation = armorStand.getLocation();
        armorStand.remove();
        armorStands.remove(armorStand);

        // Play sound and effect:
        Location effectLocation = oldLocation.add(0, 1, 0);
        PlayerUtils.playSound(effectLocation, Sound.ENDERMAN_TELEPORT, 10, 1);
        PlayerUtils.playEffect(effectLocation, Effect.ENDER_SIGNAL, null);

        // Spawn a new armor stand:
        armorStand = spawnArmorStand();
        armorStands.add(armorStand);

        // Put the item stack into the armor stand:
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

        // Insert the old location into the list with the free armor locations:
        freeArmorStandLocations.add(oldLocation);
    }

}
