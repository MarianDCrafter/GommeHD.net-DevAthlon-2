package io.github.mariandcrafter.devathlon2.runde1.commands;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import io.github.mariandcrafter.devathlon2.runde1.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * Commands to invite a player for a match.
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
        if (args.length == 0) {
            player.sendMessage(Message.message(ChatColor.RED + "Verwendung des Befehls: /invite [Spieler]"));
            return;
        }

        Player invitedPlayer = Bukkit.getPlayer(args[0]);
        if (invitedPlayer == null) {
            player.sendMessage(Message.message(ChatColor.RED + "Der Spieler " + args[0] + " ist nicht online."));
            return;
        }
        if (Main.getPlayerManager().getInvitations().get(invitedPlayer.getUniqueId()) == player.getUniqueId()) {
            player.sendMessage(Message.message(ChatColor.RED + "Der Spieler " + invitedPlayer.getName() + " hat dich bereits eingeladen."));
            return;
        }
        if (Main.getPlayerManager().getInvitations().get(player.getUniqueId()) == invitedPlayer.getUniqueId()) {
            player.sendMessage(Message.message(ChatColor.RED + "Du hast den Spieler " + invitedPlayer.getName() + " bereits eingeladen."));
            return;
        }

        UUID invitedBefore = Main.getPlayerManager().getInvitations().get(player.getUniqueId());
        if (invitedBefore != null) {
            // If he has already invited somebody, he will reject the first invitation.
            player.sendMessage(Message.message(ChatColor.GOLD + "Du hast die Einladung an " + Bukkit.getPlayer(invitedBefore).getName() + " zurückgenommen."));
        }

        player.sendMessage(Message.message(ChatColor.GOLD + "Du hast eine Einladung an " + invitedPlayer.getName() + " gesendet."));
        invitedPlayer.sendMessage(Message.message(ChatColor.GOLD + "Du hast eine Einladung von " + player.getName() + " bekommen."));

        Main.getPlayerManager().getInvitations().put(player.getUniqueId(), invitedPlayer.getUniqueId());
    }

    /**
     * Accept an invitation.
     * @param player the player who accepts the invitation
     * @param args   the arguments of the command
     */
    private void accept(Player player, String[] args) {

        Player inviter = checkInvitationsOf(player, args);
        if (inviter == null) return;

        player.sendMessage(Message.message(ChatColor.GOLD + "Du hast die Einladung von " + inviter.getName() + " angenommen."));
        inviter.sendMessage(Message.message(ChatColor.GOLD + player.getName() + " hat deine Einladung angenommen."));

        Main.getPlayerManager().getInvitations().remove(inviter.getUniqueId());

    }

    /**
     * Deny an invitation.
     * @param player the player who denys the invitation
     * @param args   the arguments of the command
     */
    private void deny(Player player, String[] args) {

        Player inviter = checkInvitationsOf(player, args);
        if (inviter == null) return;

        player.sendMessage(Message.message(ChatColor.GOLD + "Du hast die Einladung von " + inviter.getName() + " abgelehnt."));
        inviter.sendMessage(Message.message(ChatColor.GOLD + player.getName() + " hat deine Einladung abgelehnt."));

        Main.getPlayerManager().getInvitations().remove(inviter.getUniqueId());
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
                player.sendMessage(Message.message(ChatColor.RED + "Du wurdest von niemandem eingeladen oder du hast mehrere Einladungen erhalten, weshalb du zusätzlich einen Spieler angeben musst (/deny [Spieler])"));
                return null;
            }
        } else {
            inviter = Bukkit.getPlayer(args[0]);

            if (Main.getPlayerManager().getInvitations().get(inviter.getUniqueId()) != player.getUniqueId()) {
                player.sendMessage(Message.message(ChatColor.RED + "Du wurdest nicht von " + args[0] + " eingeladen."));
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

        for (Map.Entry<UUID, UUID> entry : Main.getPlayerManager().getInvitations().entrySet()) {
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