package io.github.mariandcrafter.devathlon2.runde3.game;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Gamemode<GameType extends Game> implements Buyable {

    private Area area;
    protected List<GameType> games = new ArrayList<GameType>();

    public Gamemode(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public abstract void bought(Offer offer, Player player);

    public void stoppedGame(GameType game) {
        games.remove(game);
        Main.getGameManager().getGames().remove(game.getUuid());
    }

}
