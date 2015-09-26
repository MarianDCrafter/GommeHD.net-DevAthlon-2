package io.github.mariandcrafter.devathlon2.runde3.game;

import io.github.mariandcrafter.bukkitpluginapi.messages.Message;
import io.github.mariandcrafter.bukkitpluginapi.utils.BukkitUtils;
import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Villager;

import java.util.*;

public class GameManager implements Runnable {

    public enum Phase {
        LOBBY,
        INGAME
    }

    private final static int LOBBY_TIME = 60;
    private final static double SILVERFISHES_PER_SECOND = 0.05;

    private Phase phase;
    private int time;
    private double silverfishesPerSecond;

    private List<UUID> playing = new ArrayList<UUID>();
    private Map<Villager, VillagerType> villagers = new HashMap<Villager, VillagerType>();
    private Map<UUID, Game> games = new HashMap<UUID, Game>();

    private Map<UUID, Villager> lastOfferVillagers = new HashMap<UUID, Villager>();

    public GameManager() {
        phase = Phase.LOBBY;
        time = LOBBY_TIME;
        spawnVillagers();
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 20, 20);
    }

    private void spawnVillagers() {
        for (Map.Entry<Location, VillagerType> entry : Main.getConfiguration().getVillagerSpawns().entrySet()) {
            System.out.println(entry.getKey());
            Villager villager = (Villager) entry.getKey().getWorld().spawnEntity(entry.getKey(), EntityType.VILLAGER);
            villager.setProfession(entry.getValue().getProfession());
            villagers.put(villager, entry.getValue());
            System.out.println(villager.getLocation());
        }
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
            silverfishesPerSecond += 0.001;
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

    public void joinLobby(Player player) {
        player.teleport(Main.getConfiguration().getLobbySpawn());
        // TODO PlayerUtils.clear(player);
    }

    public void joinGame(Player player) {
        playing.add(player.getUniqueId());
        player.teleport(Main.getConfiguration().getMapSpawn());
    }

    public void removePlayerFromGame(Player player) {
        System.out.println(2);
        if (playing.contains(player.getUniqueId())) {
            System.out.println(3);
            playing.remove(player.getUniqueId());
            lastOfferVillagers.remove(player.getUniqueId());
            Bukkit.broadcastMessage("§6" + player.getName() + " §4ist gestorben!");
            System.out.println(4);
        }

        System.out.println(5);
        if (phase == Phase.INGAME && playing.size() == 1) {
            System.out.println(6);
            Bukkit.broadcastMessage("§9Das Spiel ist beendet.");
            Bukkit.broadcastMessage("§9Gewonnen hat §l" + Bukkit.getPlayer(playing.get(0)).getName() + "§9.");
            phase = Phase.LOBBY;

            System.out.println(7);
            for (UUID uuid : playing) {
                joinLobby(Bukkit.getPlayer(uuid));
            }
            playing.clear();
            lastOfferVillagers.clear();

            System.out.println(8);
            Iterator<Game> iterator = games.values().iterator();
            while (iterator.hasNext()) {
                Game game = iterator.next();
                iterator.remove();
                game.stop();
            }
        }
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

    public List<UUID> getPlaying() {
        return playing;
    }

    public Map<Villager, VillagerType> getVillagers() {
        return villagers;
    }

    public Map<UUID, Game> getGames() {
        return games;
    }

    public Map<UUID, Villager> getLastOfferVillagers() {
        return lastOfferVillagers;
    }

}
