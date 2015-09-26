package io.github.mariandcrafter.devathlon2.runde3.game;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class Gamemode<GameType extends Game> implements Buyable {

    private Area area;
    protected List<GameType> games = new ArrayList<GameType>();
    private Map<UUID, Long> cooldownStarts = new HashMap<UUID, Long>();

    public Gamemode(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public abstract void bought(Offer offer, Player player);

    protected boolean cooldownIsRunning(Player player, Offer offer) {
        if (cooldownStarts != null && cooldownStarts.containsKey(player.getUniqueId()) &&
                System.currentTimeMillis() - cooldownStarts.get(player.getUniqueId()) < 60000) {
            player.sendMessage("§cDu musst immer eine Minute warten, wenn du dasselbe Minispiel spielen willst.");
            player.getInventory().addItem(new ItemStack(Material.BREAD, offer.getCosts()));
            return true;
        }
        return false;
    }

    public void stoppedGame(GameType game) {
        games.remove(game);
        for (UUID uuid : game.getUuids()) {
            Main.getGameManager().getGames().remove(uuid);
            cooldownStarts.put(uuid, System.currentTimeMillis());
        }
    }

    public void resetCooldowns() {
        cooldownStarts.clear();
    }

}
