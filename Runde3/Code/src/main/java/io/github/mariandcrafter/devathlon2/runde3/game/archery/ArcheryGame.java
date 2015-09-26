package io.github.mariandcrafter.devathlon2.runde3.game.archery;

import io.github.mariandcrafter.devathlon2.runde3.game.Game;
import io.github.mariandcrafter.devathlon2.runde3.game.Gamemode;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ArcheryGame extends Game {

    private int remainingArrows;
    private int hitBlocks;

    public ArcheryGame(Gamemode<Game> gamemode, Player player, int shots) {
        super(Arrays.asList(player.getUniqueId()), gamemode);

        this.remainingArrows = shots;
        this.hitBlocks = 0;

        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.ARROW, shots));
    }

    public void hit(Location location) {
        remainingArrows--;
        hitBlocks++;
        getPlayer().sendMessage("§7[Bogenschießen] §aSehr schön, du hast getroffen!");
        getPlayer().playSound(getPlayer().getLocation(), Sound.FIREWORK_LAUNCH, 10, 1);
        getPlayer().playEffect(getPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES, null);

        if (remainingArrows <= 0) {
            endGame();
        }
    }

    public void fail() {
        remainingArrows--;
        getPlayer().sendMessage("§7[Bogenschießen] §5Das war wohl nichts!");
        getPlayer().playSound(getPlayer().getLocation(), Sound.VILLAGER_NO, 10, 1);

        if (remainingArrows <= 0) {
            endGame();
        }
    }

    private void endGame() {
        reset();
        gamemode.stoppedGame(this);
        getPlayer().sendMessage("§7[Bogenschießen] §eDas Bogenschießen ist beendet.");

        int breads = (int) Math.ceil(hitBlocks * 0.75);
        getPlayer().getInventory().addItem(new ItemStack(Material.BREAD, breads));
        getPlayer().sendMessage("§7[Bogenschießen] §aDu bekommst " + breads + " Brote.");
    }

    protected void reset() {
        getPlayer().getInventory().remove(Material.BOW);
        getPlayer().getInventory().remove(Material.ARROW);
    }

}
