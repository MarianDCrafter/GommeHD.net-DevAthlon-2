package io.github.mariandcrafter.devathlon2.runde2.commands;

import io.github.mariandcrafter.devathlon2.runde2.Main;
import io.github.mariandcrafter.devathlon2.runde2.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * Commands to invite a player for a match.
 * Mainly copied from round one of the DevAthlon.
 */
public class StartGameCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl verwenden.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equals("invite")) {
            invite(player, args);
            return true;

        } else if (command.getName().equals("accept")) {
            accept(player, args);
            return true;

        } else if (command.getName().equals("deny")) {
            deny(player, args);
            return true;

        } else if (command.getName().equals("loc")) { // TODO remove!
            Location loc = player.getLocation();
            sender.sendMessage(loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch());
            return true;

        } else {
            return false;
        }
    }

    /**
     * Invite a player.
     *
     * @param player the player who invites another player
     * @param args   the arguments of the command
     */
    private void invite(Player player, String[] args) {
        if (Main.getGameManager().isPlaying(player)) {
            MessageUtils.error(player, "Du bist gerade in einem Spiel.");
            return;
        }
        if (args.length == 0) {
            MessageUtils.error(player, "Verwendung des Befehls: /invite [Spieler]");
            return;
        }

        Player invitedPlayer = Bukkit.getPlayer(args[0]);
        if (invitedPlayer == null) {
            MessageUtils.error(player, "Der Spieler " + args[0] + " ist nicht online.");
            return;
        }
        if (Main.getGameManager().getInvitations().get(invitedPlayer.getUniqueId()) == player.getUniqueId()) {
            MessageUtils.error(player, "Der Spieler " + invitedPlayer.getName() + " hat dich bereits eingeladen.");
            return;
        }
        if (Main.getGameManager().getInvitations().get(player.getUniqueId()) == invitedPlayer.getUniqueId()) {
            MessageUtils.error(player, "Du hast den Spieler " + invitedPlayer.getName() + " bereits eingeladen.");
            return;
        }
        if (Main.getGameManager().isPlaying(invitedPlayer)) {
            MessageUtils.error(player, "Der Spieler " + invitedPlayer.getName() + " ist gerade in einem Spiel.");
            return;
        }

        UUID invitedBefore = Main.getGameManager().getInvitations().get(player.getUniqueId());
        if (invitedBefore != null) {
            // If he has already invited somebody, he will reject the first invitation.
            MessageUtils.info(player, "Du hast die Einladung an " + Bukkit.getPlayer(invitedBefore).getName() + " zurückgenommen.");
        }

        MessageUtils.info(player, "Du hast eine Einladung an " + invitedPlayer.getName() + " gesendet.");
        MessageUtils.info(invitedPlayer, "Du hast eine Einladung von " + player.getName() + " bekommen.");

        Main.getGameManager().getInvitations().put(player.getUniqueId(), invitedPlayer.getUniqueId());
    }

    /**
     * Accept an invitation.
     * @param player the player who accepts the invitation
     * @param args   the arguments of the command
     */
    private void accept(Player player, String[] args) {
        if (Main.getGameManager().isPlaying(player)) {
            MessageUtils.error(player, "Du bist gerade in einem Spiel.");
            return;
        }

        Player inviter = checkInvitationsOf(player, args);
        if (inviter == null) return;

        if(!Main.getGameManager().startMatch(player, inviter)) {
            MessageUtils.error(player, "Du kannst die Einladung von " + inviter.getName() + " nicht annehmen, da keine Map frei ist.");
            MessageUtils.error(inviter, player.getName() + " konnte deine Einladung nicht annehmen, da keine Map frei ist.");
            return;
        }

        Main.getGameManager().getInvitations().remove(inviter.getUniqueId());
    }

    /**
     * Deny an invitation.
     * @param player the player who denys the invitation
     * @param args   the arguments of the command
     */
    private void deny(Player player, String[] args) {
        if (Main.getGameManager().isPlaying(player)) {
            MessageUtils.error(player, "Du bist gerade in einem Spiel.");
            return;
        }

        Player inviter = checkInvitationsOf(player, args);
        if (inviter == null) return;

        MessageUtils.info(player, "Du hast die Einladung von " + inviter.getName() + " abgelehnt.");
        MessageUtils.info(inviter, player.getName() + " hat deine Einladung abgelehnt.");

        Main.getGameManager().getInvitations().remove(inviter.getUniqueId());
    }

    /**
     * Checks whether the given player has an invitation.
     * If there is no argument, this will only succeed if he has only ONE invitation.
     * If there is an argument, this will only succeed if he was invited by the player with the name of the argument.
     * If this doesn't succeed, the player will get an error message.
     *
     * @param player the player who sent the command
     * @param args the arguments of the command
     * @return {@code null} if a he has no correct invitation, otherwise the player who invited him
     */
    private Player checkInvitationsOf(Player player, String[] args) {

        Player inviter;

        if (args.length == 0) {
            inviter = getPlayerWhoInvited(player.getUniqueId());
            if (inviter == null) {
                // If he hasn't been invited by someone OR if there are more than one invitations, the method
                // getPlayerWhoInvited() returns null.
                MessageUtils.error(player, "Du wurdest von niemandem eingeladen oder du hast mehrere Einladungen erhalten, weshalb du zusätzlich einen Spieler angeben musst (/deny [Spieler])");
                return null;
            }
        } else {
            inviter = Bukkit.getPlayer(args[0]);

            if (Main.getGameManager().getInvitations().get(inviter.getUniqueId()) != player.getUniqueId()) {
                MessageUtils.error(player, "Du wurdest nicht von " + args[0] + " eingeladen.");
                return null;
            }
        }

        return inviter;
    }

    /**
     * Checks whether the player with the given UUID has an invitation. Only if there is ONE invitation, this method
     * will return the player who invited him.
     *
     * @param uuid the UUID of the player to check
     * @return the player who invited the player with the given UUID.
     */
    private Player getPlayerWhoInvited(UUID uuid) {

        int count = 0;
        UUID inviter = null;

        for (Map.Entry<UUID, UUID> entry : Main.getGameManager().getInvitations().entrySet()) {
            if (entry.getValue() == uuid) {
                count++;
                inviter = entry.getKey();
            }
        }

        if (count == 1) {
            return Bukkit.getPlayer(inviter);
        } else {
            return null;
        }
    }

}
