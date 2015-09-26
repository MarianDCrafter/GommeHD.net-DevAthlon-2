package io.github.mariandcrafter.devathlon2.runde3.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class Game {

    protected List<UUID> uuids;
    protected Gamemode gamemode;

    public Game(List<UUID> uuids, Gamemode gamemode) {
        this.uuids = uuids;
        this.gamemode = gamemode;
    }

    public List<UUID> getUuids() {
        return uuids;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuids.get(0));
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    protected abstract void reset();

    public void stop() {
        reset();
        gamemode.stoppedGame(this);
    }

    public void playerLeftArea() {
        reset();
        gamemode.stoppedGame(this);
        getPlayer().sendMessage("§cDu hast den Bereich für dieses Spiel verlassen.");
    }

}
