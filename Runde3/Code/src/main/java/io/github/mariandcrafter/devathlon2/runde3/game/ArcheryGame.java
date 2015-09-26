package io.github.mariandcrafter.devathlon2.runde3.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArcheryGame extends Game {

    private int remainingArrows;
    private int hitBlocks;

    public ArcheryGame(Gamemode<Game> gamemode, Player player, int shots) {
        super(player.getUniqueId(), gamemode);
        this.remainingArrows = shots;
        this.hitBlocks = 0;

        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.ARROW, shots));
    }

    public void hit() {
        remainingArrows--;
        hitBlocks++;
        getPlayer().sendMessage("Sehr schön, du hast getroffen!");

        if (remainingArrows <= 0) {
            endGame();
        }
    }

    public void fail() {
        remainingArrows--;
        getPlayer().sendMessage("Das war wohl nichts!");

        if (remainingArrows <= 0) {
            endGame();
        }
    }

    private void endGame() {
        reset();
        gamemode.stoppedGame(this);
        getPlayer().sendMessage("Das Bogenschießen ist beendet.");
        getPlayer().getInventory().addItem(new ItemStack(Material.BREAD, hitBlocks));
        getPlayer().sendMessage("Du bekommst " + hitBlocks + " Brote.");
    }

    protected void reset() {
        getPlayer().getInventory().remove(Material.BOW);
        getPlayer().getInventory().remove(Material.ARROW);
    }

}
