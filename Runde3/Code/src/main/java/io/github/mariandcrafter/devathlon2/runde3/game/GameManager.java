package io.github.mariandcrafter.devathlon2.runde3.game;

import io.github.mariandcrafter.bukkitpluginapi.messages.Message;
import io.github.mariandcrafter.bukkitpluginapi.utils.BukkitUtils;
import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager implements Runnable {

    public enum Phase {
        LOBBY,
        INGAME
    }

    private final static int LOBBY_TIME = 60;

    private Phase phase;
    private int time;

    private List<UUID> playing = new ArrayList<UUID>();

    public GameManager() {
        phase = Phase.LOBBY;
        time = LOBBY_TIME;
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 20, 20);
    }

    @Override
    public void run() {
        if (phase == Phase.LOBBY) {
            time--;

            if (time % 10 == 0 || time <= 5) {
                Main.messageSender().send(new Message.Builder("Das Spiel startet in " + time + " Sekunden!")
                        .setReceivers(BukkitUtils.getOnlinePlayers()).build());
            }

            if (time == 0) {
                if (BukkitUtils.getOnlinePlayers().size() >= 2) {
                    startGame();
                } else {
                    Main.messageSender().send(new Message.Builder("Es sind nicht genug Spieler online.")
                            .setReceivers(BukkitUtils.getOnlinePlayers()).build());
                    phase = Phase.LOBBY;
                    time = LOBBY_TIME + 1;
                }
            }
        }
    }

    private void startGame() {
        Main.messageSender().send(new Message.Builder("Das Spiel startet jetzt!")
                .setReceivers(BukkitUtils.getOnlinePlayers()).build());
        phase = Phase.INGAME;

        for (Player player : BukkitUtils.getOnlinePlayers()) {
            joinGame(player);
        }
    }

    public void onJoin(Player player) {
        player.teleport(Main.getConfiguration().getLobbySpawn());
        // TODO PlayerUtils.clear(player);
    }

    public void joinGame(Player player) {
        playing.add(player.getUniqueId());
        player.teleport(Main.getConfiguration().getMapSpawn());
    }

}
