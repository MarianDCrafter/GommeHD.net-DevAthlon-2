package io.github.mariandcrafter.devathlon2.runde1;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private Map<UUID, UUID> invitations = new HashMap<UUID, UUID>();

    public void onJoin(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(Main.getConfiguration().getSpawn());
    }

    public void onQuit(Player player) {
        UUID uuid = player.getUniqueId();

        invitations.remove(player.getUniqueId());
        for (Map.Entry<UUID, UUID> entry : invitations.entrySet()) {
            if (entry.getValue() == uuid) {
                invitations.remove(entry.getKey());
            }
        }
    }

    public Map<UUID, UUID> getInvitations() {
        return invitations;
    }

}
