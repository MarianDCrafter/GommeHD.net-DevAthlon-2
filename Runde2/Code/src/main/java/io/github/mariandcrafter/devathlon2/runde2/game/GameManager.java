package io.github.mariandcrafter.devathlon2.runde2.game;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages the matches.
 */
public class GameManager {

    /**
     * the invitations of the players
     */
    private Map<UUID, UUID> invitations = new HashMap<UUID, UUID>();
    /**
     * currently running matches
     */
    private List<Match> matches = new ArrayList<Match>();

    /**
     * @return the list with all invitations
     */
    public Map<UUID, UUID> getInvitations() {
        return invitations;
    }

    /**
     * Called when a player joined the server. Teleports him to the spawn and sends him the join messages.
     *
     * @param player the player who joined the server
     */
    public void onJoin(Player player) {
        player.teleport(Main.getConfiguration().getSpawn());
        PlayerUtils.clear(player);

        MessageUtils.success("Willkommen in der Zukunft!", player);
        MessageUtils.info("Fordere mit /invite jemanden heraus.", player);
        MessageUtils.info("Nimm mit /accept eine Einladung an.", player);
        MessageUtils.success(ChatColor.BOLD + "Viel Spaß!", player);
    }

    /**
     * Called when a player quits. Removes the invitations of the player and removes him from his current match.
     *
     * @param player the player who quits
     */
    public void onQuit(Player player) {

        // remove invitations:
        UUID uuid = player.getUniqueId();
        invitations.remove(uuid);
        for (Map.Entry<UUID, UUID> entry : invitations.entrySet())
            if (entry.getValue() == uuid)
                invitations.remove(entry.getKey());

        // remove him from the match
        Match match = getMatch(player);
        if (match != null)
            match.playerLeft();
    }

    /**
     * Checks whether the given player is in a match.
     *
     * @param player the player to check
     * @return {@code true} if he is in a match, otherwise {@code false}
     */
    public boolean isPlaying(Player player) {
        return getMatch(player) != null;
    }

    /**
     * Used to get the current match of a player.
     *
     * @param player the player to check
     * @return the current match of the player if he is in a match, otherwise {@code null}
     */
    public Match getMatch(Player player) {
        UUID uuid = player.getUniqueId();
        for (Match match : matches)
            if (match.getRunner() == uuid || match.getCatcher() == uuid)
                return match;
        return null;
    }

    /**
     * Starts a new match with the two given players.
     * @return {@code true} if the match can start, otherwise {@code false}
     */
    public boolean startMatch(Player player1, Player player2) {

        GameMap map = getRandomFreeMap();
        if(map == null)
            return false;

        // Random roles:
        Match match;
        if (Main.getRandom().nextBoolean())
            match = new Match(map, player1.getUniqueId(), player2.getUniqueId());
        else
            match = new Match(map, player2.getUniqueId(), player1.getUniqueId());

        matches.add(match);

        match.start();

        return true;
    }

    /**
     * Removes the given match and teleports the players to the lobby spawn.
     *
     * @param match the match to remove
     */
    public void stopMatch(Match match) {
        matches.remove(match);

        match.getCatcherPlayer().teleport(Main.getConfiguration().getSpawn());
        match.getRunnerPlayer().teleport(Main.getConfiguration().getSpawn());

        PlayerUtils.clear(match.getCatcherPlayer());
        PlayerUtils.clear(match.getRunnerPlayer());
    }

    /**
     * @return a random and currently not used map or {@code null} if there is no free map
     */
    private GameMap getRandomFreeMap() {
        List<GameMap> maps = new ArrayList<GameMap>(Main.getConfiguration().getGameMaps());
        for (Match match : matches)
            maps.remove(match.getGameMap());

        if (maps.size() == 0)
            return null;

        return maps.get(Main.getRandom().nextInt(maps.size()));
    }

}
