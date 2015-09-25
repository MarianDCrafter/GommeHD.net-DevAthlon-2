package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

import java.util.List;

public class Archery implements Listener {

    private List<Location> blocks;
    private Location currentBlock;

    private List<ArcheryGame> games;

    public Archery(List<Location> blocks) {
        resetBlocks();
        chooseRandomBlock();
    }

    private void resetBlocks() {
        for (Location block : blocks) {
            block.getBlock().setType(Material.HAY_BLOCK);
        }
    }

    private void chooseRandomBlock() {
        currentBlock.getBlock().setType(Material.HAY_BLOCK);
        currentBlock = blocks.get(Main.getRandom().nextInt(blocks.size()));
        currentBlock.getBlock().setType(Material.STAINED_CLAY);
        //noinspection deprecation
        currentBlock.getBlock().setData((byte) 14);


    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();

        if (!(arrow.getShooter() instanceof Player)) return;
        Player player = (Player) arrow.getShooter();

        BlockIterator blockIterator = new BlockIterator(arrow.getWorld(), arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 4);
        Block block = null;
        while(blockIterator.hasNext()) {
            block = blockIterator.next();
            if (block.getType().isSolid()) break; // non-solid blocks are not allowed
        }

        for (ArcheryGame game : games) {
            if (game.getUuid() == player.getUniqueId()) {
                game.setRemainingArrows(game.getRemainingArrows() - 1);
                if (block.getLocation().equals(currentBlock)) {
                    game.setHitBlocks(game.getHitBlocks() + 1);
                }
            }
        }
    }

}
