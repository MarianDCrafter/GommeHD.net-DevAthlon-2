package io.github.mariandcrafter.devathlon2.runde3.game;

import org.bukkit.entity.Villager;

public enum VillagerType {

    ARCHERY("Mal Schießen", Villager.Profession.BLACKSMITH, "Bogenschießen"),
    HEALER_CHEAP("Medizin", Villager.Profession.LIBRARIAN, "Arzt"),
    HEALER_EXPENSIVE("Medizin", Villager.Profession.LIBRARIAN, "Arzt"),
    WITCHHUNT("Spiel", Villager.Profession.PRIEST, "Hexenjagd");

    private String numberString;
    private Villager.Profession profession;
    private String name;

    VillagerType(String numberString, Villager.Profession profession, String name) {
        this.numberString = numberString;
        this.profession = profession;
        this.name = name;
    }

    public String getNumberString() {
        return numberString;
    }

    public Villager.Profession getProfession() {
        return profession;
    }

    public String getName() {
        return name;
    }

}
