package io.github.mariandcrafter.devathlon2.runde1.game;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
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
        UUID uuid = player.getUniqueId();
        for (Match match : matches) {
            if (match.getRunner() == uuid || match.getCatcher() == uuid) {
                return true;
            }
        }
        return false;
    }

    public void startMatch(Player player1, Player player2) {
        Match match;
        if (random.nextBoolean()) match = new Match(getRandomFreeMap(), player1.getUniqueId(), player2.getUniqueId());
        else match = new Match(getRandomFreeMap(), player2.getUniqueId(), player1.getUniqueId());

        matches.add(match);

        match.start();
    }

    private GameMap getRandomFreeMap() {
        List<GameMap> maps = new ArrayList<GameMap>(Main.getConfiguration().getGameMaps());
        for (Match match : matches) {
            maps.remove(match.getGameMap());
        }
        return maps.get(random.nextInt(maps.size()));
    }

    public void blockHitByArrow(Arrow arrow, Block block, Player player) {
        for (Match match : matches) {
            if (match.getRunnerPlayer() == player) {
                match.runnerHitBlock(arrow, block);
                break;
            }
        }
    }

}
