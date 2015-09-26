package io.github.mariandcrafter.devathlon2.runde3.game;

import io.github.mariandcrafter.bukkitpluginapi.messages.Message;
import io.github.mariandcrafter.bukkitpluginapi.utils.BukkitUtils;
import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

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
    private List<Silverfish> silverfishes = new ArrayList<Silverfish>();

    public GameManager() {
        phase = Phase.LOBBY;
        time = LOBBY_TIME;
        spawnVillagers();
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 20, 20);
    }

    private void spawnVillagers() {
        for (Map.Entry<Location, VillagerType> entry : Main.getConfiguration().getVillagerSpawns().entrySet()) {
            Villager villager = (Villager) entry.getKey().getWorld().spawnEntity(entry.getKey(), EntityType.VILLAGER);
            villager.setProfession(entry.getValue().getProfession());
            villager.setCustomName(entry.getValue().getName());
            villager.setCustomNameVisible(true);
            villagers.put(villager, entry.getValue());
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
            Iterator<Silverfish> iterator = silverfishes.iterator();
            while (iterator.hasNext()) {
                Silverfish silverfish = iterator.next();
                if (!silverfish.isValid()) iterator.remove();
                silverfish.getLocation().getWorld().playEffect(silverfish.getLocation(), Effect.FLAME, 5);
            }

            int silverfishCount = silverfishCount();
            for (int i = 0; i < silverfishCount; i++) {
                Silverfish silverfish = (Silverfish) Bukkit.getWorld("world").spawnEntity(randomSilverfishLocation(), EntityType.SILVERFISH);
                silverfish.setTarget(randomPlayingPlayer());
                silverfishes.add(silverfish);
            }
            silverfishesPerSecond += 0.015;
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
        PlayerUtils.clear(player);
    }

    public void joinGame(Player player) {
        playing.add(player.getUniqueId());
        player.teleport(Main.getConfiguration().getMapSpawn());
        player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
        player.getInventory().addItem(new ItemStack(Material.BREAD, 3));
        player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
    }

    public void removePlayerFromGame(Player player) {
        if (playing.contains(player.getUniqueId())) {
            playing.remove(player.getUniqueId());
            lastOfferVillagers.remove(player.getUniqueId());
            if (games.containsKey(player.getUniqueId()))
                games.get(player.getUniqueId()).stop();
            Bukkit.broadcastMessage("§6" + player.getName() + " §4ist gestorben!");
        }

        if (phase == Phase.INGAME && playing.size() == 1) {
            Bukkit.broadcastMessage("§9Das Spiel ist beendet.");
            Bukkit.broadcastMessage("§9Gewonnen hat §l" + Bukkit.getPlayer(playing.get(0)).getName() + "§9.");
            phase = Phase.LOBBY;
            time = LOBBY_TIME;

            Iterator<Game> iterator = games.values().iterator();
            while (iterator.hasNext()) {
                Game game = iterator.next();
                iterator.remove();
                game.stop();
            }

            for (UUID uuid : playing) {
                joinLobby(Bukkit.getPlayer(uuid));
            }
            playing.clear();
            lastOfferVillagers.clear();
            Main.getConfiguration().getArchery().resetCooldowns();
            Main.getConfiguration().getWitchhunt().resetCooldowns();

            for (Silverfish silverfish : silverfishes) silverfish.remove();
            silverfishes.clear();
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
