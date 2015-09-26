package io.github.mariandcrafter.devathlon2.runde3.game.archery;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.game.Area;
import io.github.mariandcrafter.devathlon2.runde3.game.Gamemode;
import io.github.mariandcrafter.devathlon2.runde3.game.Offer;
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

public class Archery extends Gamemode<ArcheryGame> implements Listener {

    private List<Location> blocks;
    private Location currentBlock;

    public Archery(Area area, List<Location> blocks) {
        super(area);
        this.blocks = blocks;
        resetBlocks();
        chooseRandomBlock();
    }

    private void resetBlocks() {
        System.out.println(blocks);
        for (Location block : blocks) {
            block.getBlock().setType(Material.HAY_BLOCK);
        }
    }

    private void chooseRandomBlock() {
        if (currentBlock != null)
            currentBlock.getBlock().setType(Material.HAY_BLOCK);
        currentBlock = blocks.get(Main.getRandom().nextInt(blocks.size()));
        currentBlock.getBlock().setType(Material.STAINED_CLAY);
        //noinspection deprecation
        currentBlock.getBlock().setData((byte) 14);
    }

    @Override
    public void bought(Offer offer, Player player) {
        if (cooldownIsRunning(player, offer)) return;

        ArcheryGame game = new ArcheryGame((Gamemode) this, player, offer.getNumber());
        games.add(game);
        Main.getGameManager().getGames().put(player.getUniqueId(), game);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();
        arrow.remove();

        if (!(arrow.getShooter() instanceof Player)) return;
        Player player = (Player) arrow.getShooter();

        BlockIterator blockIterator = new BlockIterator(arrow.getWorld(), arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 4);
        Block block = null;
        while (blockIterator.hasNext()) {
            block = blockIterator.next();
            if (block.getType().isSolid()) break; // non-solid blocks are not allowed
        }

        for (ArcheryGame game : games) {
            if (game.getUuids().get(0) == player.getUniqueId()) {
                if (block.getLocation().equals(currentBlock))
                    game.hit(block.getLocation());
                else
                    game.fail();
                chooseRandomBlock();
                break;
            }
        }
    }

}
