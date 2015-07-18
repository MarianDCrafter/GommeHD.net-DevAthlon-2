package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GrenadeListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball)) return;

        Snowball snowball = (Snowball) event.getEntity();

        if (!(snowball.getShooter() instanceof Player)) return;

        Player player = (Player) snowball.getShooter();
        Match match = Main.getGameManager().getMatch(player);

        if (match == null || match.getPhase() != Match.Phase.RUNNING) return;

        supplyBlindessIfHit(match.getRunnerPlayer(), snowball);
        supplyBlindessIfHit(match.getCatcherPlayer(), snowball);

        snowball.getLocation().getWorld().playEffect(snowball.getLocation(), Effect.SPLASH, 20);
        snowball.getLocation().getWorld().playSound(snowball.getLocation(), Sound.SPLASH, 10, 1);
    }

    private void supplyBlindessIfHit(Player player, Snowball snowball) {
        if (snowball.getLocation().distance(player.getLocation()) <= 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 50));
            MessageUtils.error(player, "Du wurdest von einer Blindheits-Granate getroffen und bist jetzt für 10 Sekunden blind!");
        }
    }

}
