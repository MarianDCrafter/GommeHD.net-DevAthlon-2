package io.github.mariandcrafter.devathlon2.runde3.game;

import org.bukkit.entity.Villager;

public enum VillagerType {

    ARCHERY("Mal Schießen", Villager.Profession.BLACKSMITH);

    private String numberString;
    private Villager.Profession profession;

    VillagerType(String numberString, Villager.Profession profession) {
        this.numberString = numberString;
        this.profession = profession;
    }

    public String getNumberString() {
        return numberString;
    }

    public Villager.Profession getProfession() {
        return profession;
    }
}
