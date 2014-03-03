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
