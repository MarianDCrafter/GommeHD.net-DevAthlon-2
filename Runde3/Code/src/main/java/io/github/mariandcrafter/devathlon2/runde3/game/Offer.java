package io.github.mariandcrafter.devathlon2.runde3.game;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Offer {

    public static List<Offer> OFFERS = new ArrayList<Offer>();
    static {
        Archery archery = Main.getConfiguration().getArchery();
        OFFERS.add(new Offer(VillagerType.ARCHERY, 2, 1, archery));
        OFFERS.add(new Offer(VillagerType.ARCHERY, 5, 2, archery));
        OFFERS.add(new Offer(VillagerType.ARCHERY, 8, 3, archery));
    }

    public static List<Offer> offersForVillagerType(VillagerType villagerType) {
        List<Offer> offers = new ArrayList<Offer>();

        for (Offer offer : OFFERS) {
            if (offer.villagerType == villagerType)
                offers.add(offer);
        }

        return offers;
    }

    private VillagerType villagerType;
    private int number;
    private int costs;
    private Gamemode gamemode;

    public Offer(VillagerType villagerType, int number, int costs, Gamemode gamemode) {
        this.villagerType = villagerType;
        this.number = number;
        this.costs = costs;
        this.gamemode = gamemode;
    }

    public VillagerType getVillagerType() {
        return villagerType;
    }

    public int getNumber() {
        return number;
    }

    public int getCosts() {
        return costs;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public void acceptOffer(Player player) {
        if (!playerHasBread(player, costs)) {
            player.sendMessage("Du hast nicht genug Brot dafür.");
            return;
        }
        player.getInventory().remove(new ItemStack(Material.BREAD, costs));

        gamemode.startGameWithOffer(this, player);
    }

    private boolean playerHasBread(Player player, int amount) {
        int playerAmount = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() == Material.BREAD)
                playerAmount += itemStack.getAmount();
        }
        return playerAmount >= amount;
    }

    @Override
    public String toString() {
        return number + " " + villagerType.getNumberString() + " für " + costs + " Brote";
    }

    public String commandString() {
        return "/acceptoffer " + OFFERS.indexOf(this);
    }

}
