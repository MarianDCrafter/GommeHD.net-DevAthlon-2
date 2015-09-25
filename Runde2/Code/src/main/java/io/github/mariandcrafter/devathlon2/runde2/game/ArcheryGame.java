package io.github.mariandcrafter.devathlon2.runde2.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ArcheryGame {

    private UUID uuid;
    private int remainingArrows;
    private int hitBlocks;

    public ArcheryGame(Player player, int shots) {
        this.uuid = player.getUniqueId();
        this.remainingArrows = shots;
        this.hitBlocks = 0;

        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.ARROW, shots));
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getRemainingArrows() {
        return remainingArrows;
    }

    public void setRemainingArrows(int remainingArrows) {
        this.remainingArrows = remainingArrows;
    }

    public int getHitBlocks() {
        return hitBlocks;
    }

    public void setHitBlocks(int hitBlocks) {
        this.hitBlocks = hitBlocks;
    }
}
