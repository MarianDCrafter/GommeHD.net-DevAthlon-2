package io.github.mariandcrafter.devathlon2.runde1.game;

import java.util.UUID;

public class Match {

    private GameMap gameMap;
    private UUID runner;
    private UUID catcher;

    public Match(GameMap gameMap, UUID runner, UUID catcher) {
        this.gameMap = gameMap;
        this.runner = runner;
        this.catcher = catcher;
    }

    public void start() {

    }

}
