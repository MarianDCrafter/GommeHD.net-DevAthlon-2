package io.github.mariandcrafter.devathlon2.runde1.game;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private Map<UUID, UUID> invitations = new HashMap<UUID, UUID>();
    private List<Match> matches = new ArrayList<Match>();
    private Random random = new Random();

    public Map<UUID, UUID> getInvitations() {
        return invitations;
    }

    public List<Match> getMatches() {
        return matches;
    }

    /**
     * Called when a player joined the server.
     *
     * @param player the player who joined the server
     */
    public void onJoin(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(Main.getConfiguration().getSpawn());
        // TODO sicherheitshalber inventory clearen, level zurücksetzen, kein feuer, keine Effekte, heilen, etc.
    }

    /**
     * Called when a player quits.
     *
     * @param player the player who quits
     */
    public void onQuit(Player player) {
        UUID uuid = player.getUniqueId();

        invitations.remove(player.getUniqueId());
        for (Map.Entry<UUID, UUID> entry : invitations.entrySet()) {
            if (entry.getValue() == uuid) {
                invitations.remove(entry.getKey());
            }
        }
    }

    public boolean isPlaying(Player player) {
        return getMatch(player) != null;
    }

    public Match getMatch(Player player) {
        UUID uuid = player.getUniqueId();
        for (Match match : matches) {
            if (match.getRunner() == uuid || match.getCatcher() == uuid) {
                return match;
            }
        }
        return null;
    }

    public void startMatch(Player player1, Player player2) {
        Match match;
        if (random.nextBoolean()) match = new Match(getRandomFreeMap(), player1.getUniqueId(), player2.getUniqueId());
        else match = new Match(getRandomFreeMap(), player2.getUniqueId(), player1.getUniqueId());

        matches.add(match);

        match.start();
    }

    public void stopMatch(Match match) {
        matches.remove(match);

        match.getCatcherPlayer().teleport(Main.getConfiguration().getSpawn());
        match.getRunnerPlayer().teleport(Main.getConfiguration().getSpawn());
    }

    private GameMap getRandomFreeMap() {
        List<GameMap> maps = new ArrayList<GameMap>(Main.getConfiguration().getGameMaps());
        for (Match match : matches) {
            maps.remove(match.getGameMap());
        }
        return maps.get(random.nextInt(maps.size()));
    }

    public boolean playerHitByArrow(Arrow arrow, Player player) {
        for (Match match : matches) {
            if (match.getCatcherPlayer() == player) {
                return match.catcherHitByArrow(arrow);
            }
        }
        return false;
    }

}
