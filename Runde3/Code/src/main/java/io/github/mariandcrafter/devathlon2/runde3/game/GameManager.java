package io.github.mariandcrafter.devathlon2.runde3.game;

import io.github.mariandcrafter.bukkitpluginapi.messages.Message;
import io.github.mariandcrafter.bukkitpluginapi.utils.BukkitUtils;
import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager implements Runnable {

    public enum Phase {
        LOBBY,
        INGAME
    }

    private final static int LOBBY_TIME = 60;
    private final static double SILVERFISHES_PER_SECOND = 0.1;

    private Phase phase;
    private int time;
    private double silverfishesPerSecond;

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
        } else {
            int silverfishCount = silverfishCount();
            for (int i = 0; i < silverfishCount; i++) {
                Silverfish silverfish = (Silverfish) Bukkit.getWorld("world").spawnEntity(randomSilverfishLocation(), EntityType.SILVERFISH);
                silverfish.setTarget(randomPlayingPlayer());
                System.out.println(silverfish);
                System.out.println(silverfish.getLocation());
                System.out.println(silverfish.getTarget());
            }
            silverfishesPerSecond *= 1.01;
        }
    }

    public void startGame() {
        Main.messageSender().send(new Message.Builder("Das Spiel startet jetzt!")
                .setReceivers(BukkitUtils.getOnlinePlayers()).build());
        phase = Phase.INGAME;
        silverfishesPerSecond = SILVERFISHES_PER_SECOND;

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

    private int silverfishCount() {
        int count = (int) Math.floor(silverfishesPerSecond);
        if (Main.getRandom().nextDouble() < silverfishesPerSecond) count++;
        return count;
    }

    private Location randomSilverfishLocation() {
        List<Location> spawns = Main.getConfiguration().getSilverfishSpawns();
        return spawns.get(Main.getRandom().nextInt(spawns.size()));
    }

    private Player randomPlayingPlayer() {
        return Bukkit.getPlayer(playing.get(Main.getRandom().nextInt(playing.size())));
    }

}
