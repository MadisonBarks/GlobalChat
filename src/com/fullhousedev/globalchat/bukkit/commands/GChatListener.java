/*
 * Copyright (C) 2014 Austin Bolstridge (WolfyTheFur)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.fullhousedev.globalchat.bukkit.commands;

import com.fullhousedev.globalchat.bukkit.GlobalChat;
import com.fullhousedev.globalchat.bukkit.PluginMessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Austin
 */
public class GChatListener implements CommandExecutor{
    
    GlobalChat plugin;
    
    public GChatListener(GlobalChat pl) {
        this.plugin = pl;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if(!(cs instanceof Player)) {
            cs.sendMessage(ChatColor.RED + "This command may only be executed from the game.");
            return true;
        }
        Player player = Player.class.cast(cs);
        if(args.length < 1) {
            printHelp(player);
            return true;
        }
        switch(args[0]) {
            case "help":
                printHelp(player);
                return true;
            case "toggle":
                if(!player.hasPermission("globalchat.toggle")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }
                if(plugin.toggledUsers.contains(player.getName().toLowerCase())) {
                    plugin.toggledUsers.remove(player.getName().toLowerCase());
                    PluginMessageManager.userToggleMessage(player.getName(), false,
                            plugin);
                    player.sendMessage(ChatColor.GOLD + "You now recieve"
                            + " global chat messages.");
                    return true;
                }
                else {
                    plugin.toggledUsers.add(player.getName().toLowerCase());
                    PluginMessageManager.userToggleMessage(player.getName(), true,
                            plugin);
                    player.sendMessage(ChatColor.GOLD + "You no longer recieve"
                            + " global chat messages.");
                    return true;
                }
            case "socialspy":
                if(!player.hasPermission("gchat.socialspy")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission");
                    return true;
                }
                if(plugin.socialspyUsers.contains(player.getName().toLowerCase())) {
                    plugin.socialspyUsers.remove(player.getName().toLowerCase());
                    PluginMessageManager.userToggleSocialspy(player.getName(), false,
                            plugin);
                    player.sendMessage(ChatColor.GOLD + "SocialSpy for global"
                            + " messages has been disabled.");
                    return true;
                }
                else {
                    plugin.socialspyUsers.add(player.getName().toLowerCase());
                    PluginMessageManager.userToggleSocialspy(player.getName(), true,
                            plugin);
                    player.sendMessage(ChatColor.GOLD + "SocialSpy for global"
                            + " messages has been enabled.");
                    return true;
                }
            case "msg":
                if(args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Usage: /gchat msg <username>"
                            + " <message>");
                    return true;
                }
                String username = args[1];
                if(plugin.toggledUsers.contains(username)) {
                    player.sendMessage(ChatColor.RED + "This user has messaging"
                            + " disabled.");
                    return true;
                }
                String message = "";
                for(int i = 2; i < args.length; i++) {
                    message += args[i];
                    message += " ";
                }
                Player playerOnServer = Bukkit.getPlayer(username);
                if(playerOnServer != null && playerOnServer.isOnline()) {
                    //Oh look! The player is on THIS server!
                    playerOnServer.sendMessage(ChatColor.GOLD + "[" + player.getName() +
                            " -> me]" + ChatColor.WHITE + message);
                    player.sendMessage(ChatColor.GOLD + "[me -> " + 
                            username + "]" + ChatColor.WHITE + message);
                    for(String ssUser : plugin.socialspyUsers) {
                        //Send to the SocSpy users on this server.
                        Player ssPlayer = Bukkit.getPlayer(ssUser);
                        if(ssPlayer != null && ssPlayer.isOnline()) {
                            ssPlayer.sendMessage("[global]" + player.getName() +
                                    " -> " + username + ": " + message);
                        }
                    }
                    PluginMessageManager.chatMessage(username, player.getName(),
                            message, plugin);
                    return true;
                }
                if(!plugin.players.containsKey(username)) {
                    //The user can't be found anywhere....
                    player.sendMessage(ChatColor.RED + "This user is not online.");
                    return true;
                }
                for(String ssUser : plugin.socialspyUsers) {
                    //Send to the SocSpy users on this server.
                    Player ssPlayer = Bukkit.getPlayer(ssUser);
                    if(ssPlayer != null && ssPlayer.isOnline()) {
                        ssPlayer.sendMessage("[global]" + player.getName() +
                                " -> " + username + ": " + message);
                    }
                }
                PluginMessageManager.chatMessage(username, player.getName(),
                        message, plugin);
                return true;
            default:
                printHelp(player);
                return true;
        }
    }
    
    private void printHelp(Player player) {
        player.sendMessage(ChatColor.RED + "Usage: /gchat <command>");
        player.sendMessage(ChatColor.RED + "Where <command> is one of the following:");
        player.sendMessage(ChatColor.RED + " - help: Get help on this command.");
        player.sendMessage(ChatColor.RED + " - msg <username> <message>:"
                + " Send <message> to <username>.");
        player.sendMessage(ChatColor.RED + " - toggle: Toggle the ability"
                + " to receive messages, if you have the permission.");
        player.sendMessage(ChatColor.RED + " - socialspy: Enable socialspy on"
                + " the global messages, if you have the permission.");
        player.sendMessage(ChatColor.GREEN + "Plugin created by WolfyTheFur");
    }
}
