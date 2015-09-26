package io.github.mariandcrafter.devathlon2.runde3.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Healer implements Buyable {

    @Override
    public void bought(Offer offer, Player player) {
        player.getInventory().addItem(new ItemStack(Material.MILK_BUCKET, 1));
    }

}
