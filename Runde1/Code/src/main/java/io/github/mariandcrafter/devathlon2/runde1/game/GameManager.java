package io.github.mariandcrafter.devathlon2.runde1.game;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private Map<UUID, UUID> invitations = new HashMap<UUID, UUID>();
    private List<Match> matches = new ArrayList<Match>();

    public Map<UUID, UUID> getInvitations() {
        return invitations;
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

}
