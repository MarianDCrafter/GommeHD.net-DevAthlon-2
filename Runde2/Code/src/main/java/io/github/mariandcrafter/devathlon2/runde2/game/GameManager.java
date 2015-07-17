package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GameManager {

    private Map<UUID, UUID> invitations = new HashMap<UUID, UUID>();
    //private List<Match> matches = new ArrayList<Match>();
    private Random random = new Random();

    public Map<UUID, UUID> getInvitations() {
        return invitations;
    }

    /*public List<Match> getMatches() {
        return matches; TODO
    }*/

    /**
     * Called when a player joined the server.
     *
     * @param player the player who joined the server
     */
    public void onJoin(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(Main.getConfiguration().getSpawn());
        PlayerUtils.clear(player);
    }

    /**
     * Checks whether the given player is in a match.
     *
     * @param player the player to check
     * @return {@code true} if he is in a match, otherwise {@code false}
     */
    public boolean isPlaying(Player player) {
        //return getMatch(player) != null; TODO
        return false;
    }

    /**
     * Used to get the current match of a player
     *
     * @param player the player to check
     * @return the current match of the player if he is in a match, otherwise {@code null}
     */
    /*public Match getMatch(Player player) {
        UUID uuid = player.getUniqueId(); TODO
        for (Match match : matches) {
            if (match.getRunner() == uuid || match.getCatcher() == uuid) {
                return match;
            }
        }
        return null;
    }*/

    /**
     * Starts a new match with the two given players.
     * @return {@code true} if the match can start, otherwise {@code false}
     */
    public boolean startMatch(Player player1, Player player2) {

        /*GameMap map = getRandomFreeMap();
        if(map == null)
            return false;

        // Random roles:
        Match match;
        if (random.nextBoolean())
            match = new Match(map, player1.getUniqueId(), player2.getUniqueId());
        else
            match = new Match(map, player2.getUniqueId(), player1.getUniqueId());

        matches.add(match);

        match.start();*/ // TODO

        return true;
    }

}
