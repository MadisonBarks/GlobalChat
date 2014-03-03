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

package com.fullhousedev.globalchat.bukkit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 *
 * @author Austin
 */
public class MessageListener implements PluginMessageListener{
    GlobalChat plugin;
    
    public MessageListener(GlobalChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] msg) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(msg));

        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("Forward")) {
                String subSubChannel = in.readUTF();
                if(subSubChannel.equals("UserSyncResp")) {
                    short len = in.readShort();
                    byte[] msgBytes = new byte[len];
                    in.readFully(msg);
                    DataInputStream customMsg = new DataInputStream(
                            new ByteArrayInputStream(msgBytes));
                    String server = customMsg.readUTF();
                    String userList = customMsg.readUTF();
                    
                    String usersToggled = customMsg.readUTF();
                    String usersSocialspy = customMsg.readUTF();
                    
                    String[] users = userList.split(", ");
                    for(String user : users) {
                        plugin.players.put(user, server);
                    }
                    
                    users = usersToggled.split(", ");
                    plugin.toggledUsers = (ArrayList<String>) Arrays.asList(users);
                    
                    users = usersSocialspy.split(", ");
                    plugin.socialspyUsers = (ArrayList<String>) Arrays.asList(users);
                }
                else if(subSubChannel.equals("UserSync")) {
                    short len = in.readShort();
                    byte[] msgBytes = new byte[len];
                    in.readFully(msg);
                    DataInputStream customMsg = new DataInputStream(
                            new ByteArrayInputStream(msgBytes));
                    String server = customMsg.readUTF();
                    
                    Player[] allPlayers = Bukkit.getOnlinePlayers();
                    StringBuilder sb = new StringBuilder();
                    for(Player playerObj : allPlayers) {
                        sb.append(playerObj.getName()).append(", ");
                    }
                    
                    StringBuilder sbToggle = new StringBuilder();
                    for(String username : plugin.toggledUsers) {
                        sbToggle.append(username).append(", ");
                    }
                    
                    StringBuilder sbSocialSpy = new StringBuilder();
                    for(String username : plugin.socialspyUsers) {
                        sbToggle.append(username).append(", ");
                    }
                    
                    PluginMessageManager.sendSyncResponse(sb.toString(),
                            sbSocialSpy.toString(), sbToggle.toString(),
                            plugin.serverName, server, plugin);
                }
                else if(subSubChannel.equals("userconnect")) {
                    short len = in.readShort();
                    byte[] msgBytes = new byte[len];
                    in.readFully(msg);
                    DataInputStream customMsg = new DataInputStream(
                            new ByteArrayInputStream(msgBytes));
                    String server = customMsg.readUTF();
                    String username = customMsg.readUTF();
                    
                    plugin.players.put(username, server);
                }
                else if(subSubChannel.equals("userdisconnect")) {
                    short len = in.readShort();
                    byte[] msgBytes = new byte[len];
                    in.readFully(msg);
                    DataInputStream customMsg = new DataInputStream(
                            new ByteArrayInputStream(msgBytes));
                    String username = customMsg.readUTF();
                    
                    plugin.players.remove(username);
                }
                else if(subSubChannel.equals("chatmessage")) {
                    short len = in.readShort();
                    byte[] msgBytes = new byte[len];
                    in.readFully(msg);
                    DataInputStream customMsg = new DataInputStream(
                            new ByteArrayInputStream(msgBytes));
                    String userTo = customMsg.readUTF();
                    String userFrom = customMsg.readUTF();
                    String message = customMsg.readUTF();
                    
                    Player playerObj = Bukkit.getPlayer(userTo);
                    if(playerObj != null && playerObj.isOnline()) {
                        playerObj.sendMessage(ChatColor.GOLD +
                                    "[" + userFrom + " -> " + userTo + "]" +
                                    message);
                    }
                    for(String username : plugin.socialspyUsers) {
                        Player ssPlayer = Bukkit.getPlayer(username);
                        if(ssPlayer != null && ssPlayer.isOnline()) {
                            ssPlayer.sendMessage("[global]" + player.getName() +
                                    " -> " + username + ": " + message);
                        }
                    }
                }
                else if(subSubChannel.equals("togglemsg")) {
                    short len = in.readShort();
                    byte[] msgBytes = new byte[len];
                    in.readFully(msg);
                    DataInputStream customMsg = new DataInputStream(
                            new ByteArrayInputStream(msgBytes));
                    String username = customMsg.readUTF();
                    boolean on = customMsg.readBoolean();
                    
                    if(on) {
                        plugin.toggledUsers.add(username.toLowerCase());
                    }
                    else {
                        plugin.toggledUsers.remove(username.toLowerCase());
                    }
                }
                else if(subSubChannel.equals("toggless")) {
                    short len = in.readShort();
                    byte[] msgBytes = new byte[len];
                    in.readFully(msg);
                    DataInputStream customMsg = new DataInputStream(
                            new ByteArrayInputStream(msgBytes));
                    String username = customMsg.readUTF();
                    boolean on = customMsg.readBoolean();
                    
                    if(on) {
                        plugin.socialspyUsers.add(username.toLowerCase());
                    }
                    else {
                        plugin.socialspyUsers.remove(username.toLowerCase());
                    }
                }
                
            } else if (subchannel.equals("GetServer")) {
                // Example: GetServer subchannel
                plugin.serverName = in.readUTF();
            }
        } catch (IOException e) {
            // There was an issue in creating the subchannel string
        }
    }
}
