package io.github.mariandcrafter.devathlon2.runde3.game.witchhunt;

import io.github.mariandcrafter.devathlon2.runde3.game.Game;
import io.github.mariandcrafter.devathlon2.runde3.game.Gamemode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WitchhuntGame extends Game {

    private Location afterSpawn;
    private Location witchSpawn;
    private List<Location> skeletonSpawns, spiderSpawns, zombieSpawns;

    private Witch witch;
    private List<Entity> mobs = new ArrayList<Entity>();

    private long startTime;

    public WitchhuntGame(Location afterSpawn, List<UUID> uuids, Gamemode gamemode, Location witchSpawn, List<Location> skeletonSpawns, List<Location> spiderSpawns, List<Location> zombieSpawns) {
        super(uuids, gamemode);
        this.afterSpawn = afterSpawn;
        this.witchSpawn = witchSpawn;
        this.skeletonSpawns = skeletonSpawns;
        this.spiderSpawns = spiderSpawns;
        this.zombieSpawns = zombieSpawns;

        startTime = System.currentTimeMillis();

        spawnMobs();
    }

    private void spawnMobs() {
        World world = afterSpawn.getWorld();

        witch = (Witch) world.spawnEntity(witchSpawn, EntityType.WITCH);
        for (Location location : skeletonSpawns)
            mobs.add(world.spawnEntity(location, EntityType.SKELETON));
        for (Location location : spiderSpawns)
            mobs.add(world.spawnEntity(location, EntityType.SPIDER));
        for (Location location : zombieSpawns)
            mobs.add(world.spawnEntity(location, EntityType.ZOMBIE));
    }

    public void witchKilled() {
        reset();
        gamemode.stoppedGame(this);
        getPlayer().sendMessage("§7[Bogenschießen] §eDu hast die Hexe getötet.");

        long length = System.currentTimeMillis() - startTime;
        int breads;
        if (length < 10000)
            breads = 6;
        else if (length < 15000)
            breads = 5;
        else if (length < 20000)
            breads = 4;
        else if (length < 30000)
            breads = 3;
        else if (length < 60000)
            breads = 2;
        else
            breads = 1;

        getPlayer().getInventory().addItem(new ItemStack(Material.BREAD, breads));
        getPlayer().sendMessage("§7[Bogenschießen] §aDu bekommst " + breads + " Brote.");
    }

    @Override
    protected void reset() {
        Player player = Bukkit.getPlayer(uuids.get(0));
        player.teleport(afterSpawn);
        player.setHealth(player.getMaxHealth());

        witch.remove();
        for (Entity mob : mobs)
            mob.remove();
    }

    public Witch getWitch() {
        return witch;
    }

}
