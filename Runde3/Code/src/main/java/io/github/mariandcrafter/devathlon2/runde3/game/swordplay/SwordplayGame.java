package io.github.mariandcrafter.devathlon2.runde3.game.swordplay;

import io.github.mariandcrafter.devathlon2.runde3.game.Game;
import io.github.mariandcrafter.devathlon2.runde3.game.Gamemode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class SwordplayGame extends Game {

    private Location afterSpawn;

    public SwordplayGame(List<UUID> uuids, Gamemode gamemode, Location afterSpawn) {
        super(uuids, gamemode);
        this.afterSpawn = afterSpawn;
    }

    public void endGameWithWinner(Player winner, Player looser) {
        winner.sendMessage("§aDu hast gewonnen und bekommst Essen.");
        looser.sendMessage("§cDu hast verloren und bekommst nichts zu Essen.");

        reset();
        gamemode.stoppedGame(this);

        winner.getInventory().addItem(new ItemStack(Material.BREAD, 4));
    }

    @Override
    protected void reset() {
        for (UUID uuid : uuids) {
            Player player = Bukkit.getPlayer(uuid);
            player.teleport(afterSpawn);
            player.setHealth(player.getMaxHealth());
        }
    }

}
