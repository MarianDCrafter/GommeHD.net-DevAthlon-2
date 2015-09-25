package io.github.mariandcrafter.devathlon2.runde3.game;

import java.util.ArrayList;
import java.util.List;

public class Offer {

    public static List<Offer> OFFERS = new ArrayList<Offer>();
    static {
        OFFERS.add(new Offer(VillagerType.ARCHERY, 2, 1));
        OFFERS.add(new Offer(VillagerType.ARCHERY, 5, 2));
        OFFERS.add(new Offer(VillagerType.ARCHERY, 8, 3));
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

    public Offer(VillagerType villagerType, int number, int costs) {
        this.villagerType = villagerType;
        this.number = number;
        this.costs = costs;
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

    @Override
    public String toString() {
        return number + " " + villagerType.getNumberString() + " für " + costs + " Brote";
    }

    public String commandString() {
        return "/acceptoffer " + OFFERS.indexOf(this);
    }

}
