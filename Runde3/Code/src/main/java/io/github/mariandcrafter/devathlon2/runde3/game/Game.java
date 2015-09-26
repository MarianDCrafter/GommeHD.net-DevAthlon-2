package io.github.mariandcrafter.devathlon2.runde3.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class Game {

    protected UUID uuid;
    protected Gamemode gamemode;

    public Game(UUID uuid, Gamemode gamemode) {
        this.uuid = uuid;
        this.gamemode = gamemode;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
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
