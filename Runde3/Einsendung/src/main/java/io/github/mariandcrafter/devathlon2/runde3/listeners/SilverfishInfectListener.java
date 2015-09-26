package io.github.mariandcrafter.devathlon2.runde3.listeners;

import io.github.mariandcrafter.devathlon2.runde3.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SilverfishInfectListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Silverfish && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!Main.getGameManager().getPlaying().contains(player.getUniqueId())) { return; }

            event.getDamager().remove();
            player.sendMessage("§cDu wurdest von einer Ratte infiziert und hast die Pest. §6Suche schnell einen Heiler auf und hole dir Medizin.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 12000, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
            player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 10, 1);
        }
    }

}
