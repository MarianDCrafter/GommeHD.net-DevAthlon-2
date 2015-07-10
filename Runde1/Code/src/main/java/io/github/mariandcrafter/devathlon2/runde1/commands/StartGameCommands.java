package io.github.mariandcrafter.devathlon2.runde1.commands;

import io.github.mariandcrafter.devathlon2.runde1.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

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

    private void invite(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Verwendung des Befehls: /invite [Spieler]");
            return;
        }

        Player invitedPlayer = Bukkit.getPlayer(args[0]);
        if (invitedPlayer == null) {
            player.sendMessage("Der Spieler " + args[0] + " ist nicht online.");
            return;
        }
        if (Main.getPlayerManager().getInvitations().get(invitedPlayer.getUniqueId()) == player.getUniqueId()) {
            player.sendMessage("Der Spieler " + args[0] + " hat dich bereits, eingeladen.");
            return;
        }
        if (Main.getPlayerManager().getInvitations().get(player.getUniqueId()) == invitedPlayer.getUniqueId()) {
            player.sendMessage("Du hast den Spieler " + args[0] + " bereits eingeladen.");
            return;
        }

        UUID invitedBefore = Main.getPlayerManager().getInvitations().get(player.getUniqueId());
        if (invitedBefore != null) {
            player.sendMessage("Du hast die Einladung an " + Bukkit.getPlayer(invitedBefore).getName() + " zurückgenommen.");
        }

        player.sendMessage("Du hast eine Einladung an " + invitedPlayer.getName() + " gesendet.");
        invitedPlayer.sendMessage("Du hast eine Einladung von " + player.getName() + " bekommen.");

        Main.getPlayerManager().getInvitations().put(player.getUniqueId(), invitedPlayer.getUniqueId());
    }

    private void accept(Player player, String[] args) {
        Player inviter;

        if (args.length == 0) {
            inviter = getPlayerWhoInvited(player.getUniqueId());
            if (inviter == null) {
                player.sendMessage("Du wurdest von niemandem eingeladen oder du hast mehrere Einladungen erhalten, weshalb du zusätzlich einen Spieler angeben musst (/deny [Spieler])");
                return;
            }
        } else {
            inviter = Bukkit.getPlayer(args[0]);

            if (Main.getPlayerManager().getInvitations().get(inviter.getUniqueId()) != player.getUniqueId()) {
                player.sendMessage("Du wurdest nicht von " + args[0] + " eingeladen.");
                return;
            }
        }

        player.sendMessage("Du hast die Einladung von " + inviter.getName() + " angenommen.");
        inviter.sendMessage(player.getName() + " hat deine Einladung angenommen.");

        Main.getPlayerManager().getInvitations().remove(inviter.getUniqueId());

    }

    private void deny(Player player, String[] args) {
        Player inviter;

        if (args.length == 0) {
            inviter = getPlayerWhoInvited(player.getUniqueId());
            if (inviter == null) {
                player.sendMessage("Du wurdest von niemandem eingeladen oder du hast mehrere Einladungen erhalten, weshalb du zusätzlich einen Spieler angeben musst (/deny [Spieler])");
                return;
            }
        } else {
            inviter = Bukkit.getPlayer(args[0]);

            if (Main.getPlayerManager().getInvitations().get(inviter.getUniqueId()) != player.getUniqueId()) {
                player.sendMessage("Du wurdest nicht von " + args[0] + " eingeladen.");
                return;
            }
        }

        player.sendMessage("Du hast die Einladung von " + inviter.getName() + " abgelehnt.");
        inviter.sendMessage(player.getName() + " hat deine Einladung abgelehnt.");

        Main.getPlayerManager().getInvitations().remove(inviter.getUniqueId());
    }

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
