package io.github.mariandcrafter.devathlon2.runde2.listeners;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.game.Match;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import io.github.mariandcrafter.devathlon2.runde2.utils.PlayerUtils;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Used to implement the blindness grenades.
 */
public class GrenadeListener implements Listener {

    /**
     * Called when a projectile hits something.
     */
    @SuppressWarnings("unused")
    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball)) return;

        Snowball snowball = (Snowball) event.getEntity();

        if (!(snowball.getShooter() instanceof Player)) return;

        Player player = (Player) snowball.getShooter();
        Match match = Main.getGameManager().getMatch(player);

        // Only allowed if the match is in phase RUNNING:
        if (match == null || match.getPhase() != Match.Phase.RUNNING) return;

        // Check if one of the players or both players have been hit by the snowball:
        supplyBlindessIfHit(match.getRunnerPlayer(), snowball);
        supplyBlindessIfHit(match.getCatcherPlayer(), snowball);

        // Play splash sound and effect:
        PlayerUtils.playSound(snowball.getLocation(), Sound.SPLASH, 10, 1);
        PlayerUtils.playEffect(snowball.getLocation(), Effect.SPLASH, 100);
    }

    /**
     * Checks whether the snowball has hit the player. This is the case if the snowball is not more than three blocks away.
     * @param player the player to check
     * @param snowball the snowball
     */
    private void supplyBlindessIfHit(Player player, Snowball snowball) {
        if (snowball.getLocation().distance(player.getLocation()) <= 3) {
            // Apply blindess effect and send message:
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 80));
            MessageUtils.error("Du wurdest von einer Blindheits-Granate getroffen und bist jetzt für 10 Sekunden blind!", player);
        }
    }

}
