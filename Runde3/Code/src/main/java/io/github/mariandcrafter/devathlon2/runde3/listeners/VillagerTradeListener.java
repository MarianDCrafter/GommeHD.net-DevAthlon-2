package io.github.mariandcrafter.devathlon2.runde3.listeners;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import io.github.mariandcrafter.devathlon2.runde3.game.Offer;
import io.github.mariandcrafter.devathlon2.runde3.game.VillagerType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.List;

public class VillagerTradeListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity rightClicked = event.getRightClicked();
        System.out.println("1");
        System.out.println(Main.getGameManager().getVillagers().containsKey(rightClicked));
        System.out.println(Main.getGameManager().getPlaying().contains(player.getUniqueId()));
        System.out.println(Main.getGameManager().getVillagers().keySet());
        System.out.println(rightClicked);
        if (!(event.getRightClicked() instanceof Villager) ||
                !Main.getGameManager().getVillagers().keySet().contains(rightClicked) ||
                !Main.getGameManager().getPlaying().contains(player.getUniqueId())) { return; }

        System.out.println("2");
        event.setCancelled(true);
        VillagerType type = Main.getGameManager().getVillagers().get(rightClicked);
        List<Offer> offers = Offer.offersForVillagerType(type);
        printOffers(player, offers);
    }

    private void printOffers(Player player, List<Offer> offers) {
        player.sendMessage("Ich habe folgende Angebote für dich:");

        for (Offer offer : offers) {
            TextComponent component = new TextComponent(offer.toString());
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, offer.commandString()));
            player.spigot().sendMessage(component);
        }

    }

}
