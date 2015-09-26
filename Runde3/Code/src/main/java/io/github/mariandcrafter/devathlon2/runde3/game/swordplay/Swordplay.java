package io.github.mariandcrafter.devathlon2.runde3.game.swordplay;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.game.Gamemode;
import io.github.mariandcrafter.devathlon2.runde3.game.Offer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Swordplay extends Gamemode {

    private Location plate1, plate2;
    private Location spawn1, spawn2, spawnAfter;

    public Swordplay(Location plate1, Location plate2, Location spawn1, Location spawn2, Location spawnAfter) {
        super(null);
        this.plate1 = plate1;
        this.plate2 = plate2;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.spawnAfter = spawnAfter;
    }

    public void tryStartAGameWith(Player player1, Player player2) {
        System.out.println(games.size());
        if (games.size() == 0 && new Offer(null, 1, 2, this).acceptTwoPlayerOffer(player1, player2)) {
            System.out.println(50);
            SwordplayGame game = new SwordplayGame(Arrays.asList(player1.getUniqueId(), player2.getUniqueId()), this, spawnAfter);
            games.add(game);
            Main.getGameManager().getGames().put(player1.getUniqueId(), game);
            Main.getGameManager().getGames().put(player2.getUniqueId(), game);

            player1.teleport(spawn1);
            player2.teleport(spawn2);
        }
    }

    @Override
    public void bought(Offer offer, Player player) {
    }

    public Location getPlate1() {
        return plate1;
    }

    public Location getPlate2() {
        return plate2;
    }
}
