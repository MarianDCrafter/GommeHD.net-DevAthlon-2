package io.github.mariandcrafter.devathlon2.runde3.game;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
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

        Healer healer = new Healer();
        OFFERS.add(new Offer(VillagerType.HEALER_EXPENSIVE, 1, 5, healer));
        OFFERS.add(new Offer(VillagerType.HEALER_CHEAP, 1, 3, healer));
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
    private Buyable buyable;

    public Offer(VillagerType villagerType, int number, int costs, Buyable buyable) {
        this.villagerType = villagerType;
        this.number = number;
        this.costs = costs;
        this.buyable = buyable;
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

    public Buyable getBuyable() {
        return buyable;
    }

    public void acceptOffer(Player player) {
        Villager lastOfferVillager = Main.getGameManager().getLastOfferVillagers().get(player.getUniqueId());
        if (lastOfferVillager == null ||
                !offersForVillagerType(Main.getGameManager().getVillagers().get(lastOfferVillager)).contains(this) ||
                player.getLocation().distance(lastOfferVillager.getLocation()) > 5) {
            player.sendMessage("§cAngebot konnte nicht angenommen werden. Gehe bitte erneut zu dem Verkäufer.");
            return;
        }

        if (!playerHasBread(player, costs)) {
            player.sendMessage("§cDu hast nicht genug Brot dafür.");
            return;
        }
        player.getInventory().removeItem(new ItemStack(Material.BREAD, costs));

        buyable.bought(this, player);
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
