package io.github.mariandcrafter.devathlon2.runde3.commands;

import io.github.mariandcrafter.bukkitpluginapi.commands.Command;
import io.github.mariandcrafter.devathlon2.runde3.Main;

public class StartCommand {

    @Command(
            command = {"start"}
    )
    public void start() {
        Main.getGameManager().startGame();
    }

}
