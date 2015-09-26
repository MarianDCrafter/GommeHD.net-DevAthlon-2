package io.github.mariandcrafter.devathlon2.runde3.game.witchhunt;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.game.Gamemode;
import io.github.mariandcrafter.devathlon2.runde3.game.Offer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Witchhunt extends Gamemode<WitchhuntGame> implements Listener {

    private Location spawn, afterSpawn;

    private Location witchSpawn;
    private List<Location> skeletonSpawns, spiderSpawns, zombieSpawns;

    private boolean isSpawning = false;

    public Witchhunt(Location spawn, Location afterSpawn, Location witchSpawn, List<Location> skeletonSpawns, List<Location> spiderSpawns, List<Location> zombieSpawns) {
        super(null);
        this.spawn = spawn;
        this.afterSpawn = afterSpawn;
        this.witchSpawn = witchSpawn;
        this.skeletonSpawns = skeletonSpawns;
        this.spiderSpawns = spiderSpawns;
        this.zombieSpawns = zombieSpawns;
    }

    @Override
    public void bought(Offer offer, Player player) {
        if (cooldownIsRunning(player, offer)) return;
        if (games.size() == 0) {
            isSpawning = true;
            WitchhuntGame game = new WitchhuntGame(afterSpawn, Arrays.asList(player.getUniqueId()), (Gamemode) this, witchSpawn, skeletonSpawns, spiderSpawns, zombieSpawns);
            isSpawning = false;

            player.teleport(spawn);
            games.add(game);
            Main.getGameManager().getGames().put(player.getUniqueId(), game);
        } else {
            player.getInventory().addItem(new ItemStack(Material.BREAD, offer.getCosts()));
        }
    }

    public boolean isSpawning() {
        return isSpawning;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onDespawn(EntityDamageEvent event) {
        if (event.getEntity() instanceof Witch && games.size() > 0 && games.get(0).getWitch() == event.getEntity() &&
                ((Witch) event.getEntity()).getHealth() - event.getDamage() <= 0) {
            games.get(0).witchKilled();
        }
    }

}
