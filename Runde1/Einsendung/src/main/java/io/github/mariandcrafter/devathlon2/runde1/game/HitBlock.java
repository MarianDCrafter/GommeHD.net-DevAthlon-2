package io.github.mariandcrafter.devathlon2.runde1.game;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

/**
 * This class holds the block that was shooted by the runner and the material and data of the block before.
 * The block changes to wool and every 2 ticks it changes the color.
 */
public class HitBlock implements Runnable {

    private Material resetMaterial;
    private byte resetData;

    private Block block;

    private BukkitTask task;

    /**
     * @param block the block shooted by the runner
     */
    public HitBlock(Block block) {
        this.resetMaterial = block.getType();
        //noinspection deprecation
        this.resetData = block.getData();
        this.block = block;
    }

    /**
     * Sets this block to wool and starts the timer which changes the color.
     */
    public void start() {
        block.setType(Material.WOOL);

        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 2L, 2L);
    }

    /**
     * Resets the block and stops the timer which changes the color.
     */
    public void stop() {
        task.cancel();

        block.setType(resetMaterial);
        //noinspection deprecation
        block.setData(resetData);
    }

    /**
     * Checks whether the given block is the hit block.
     *
     * @param block the block to check
     * @return {@code true} if the given block is the hit block, otherwise {@code false}
     */
    public boolean is(Block block) {
        return block.getLocation().equals(this.block.getLocation());
    }

    /**
     * Changes the color of the block randomly.
     */
    @Override
    public void run() {
        //noinspection deprecation
        block.setData((byte) (Math.random() * 16));

        // play effect and sound
        block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
        block.getWorld().playSound(block.getLocation(), Sound.FIREWORK_TWINKLE, 10, 1);
    }

}
