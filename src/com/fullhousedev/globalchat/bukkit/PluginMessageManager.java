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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.util.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Austin
 */
public class PluginMessageManager {
    public static void sendRawMessage(String subChannel, String serverName,
            byte[] message, Plugin pl) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Forward");
            out.writeUTF(serverName);
            out.writeUTF(subChannel);
            
            out.writeShort(message.length);
            out.write(message);
        } catch (IOException ex) {
            Logger.getLogger(GlobalChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Player p = Bukkit.getOnlinePlayers()[0];

        p.sendPluginMessage(pl, "BungeeCord", b.toByteArray());
    }
    
    public static void getServerName(Plugin pl) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("GetServer");
        } catch (IOException ex) {
            Logger.getLogger(GlobalChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        // OR, if you don't need to send it to a specific player
        Player p = Bukkit.getOnlinePlayers()[0];

        p.sendPluginMessage(pl, "BungeeCord", b.toByteArray());
    }
    
    public static void syncUsers(Plugin pl, String serverName) {
        try {
            ByteArrayOutputStream customData = new ByteArrayOutputStream();
            DataOutputStream outCustom = new DataOutputStream(customData);
            outCustom.writeUTF(serverName);
            sendRawMessage("UserSync", "ALL", customData.toByteArray(), pl);
        } catch (IOException ex) {
            Logger.getLogger(GlobalChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendSyncResponse(String playerList, String socialSpy,
                                        String toggledUsers, String serverName,
                                        String requestingServer, Plugin pl)
    {
        try {
            ByteArrayOutputStream customData = new ByteArrayOutputStream();
            DataOutputStream outCustom = new DataOutputStream(customData);
            outCustom.writeUTF(serverName);
            outCustom.writeUTF(playerList);
            
            sendRawMessage("UserSyncResp", requestingServer, 
                    customData.toByteArray(), pl);
        } catch (IOException ex) {
            Logger.getLogger(GlobalChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void userToggleMessage(String username,
            boolean on, Plugin pl) {
        try {
            ByteArrayOutputStream customData = new ByteArrayOutputStream();
            DataOutputStream outCustom = new DataOutputStream(customData);
            outCustom.writeUTF(username);
            outCustom.writeBoolean(on);
            
            sendRawMessage("togglemsg", "ALL", customData.toByteArray(), pl);
        } catch (IOException ex) {
            Logger.getLogger(GlobalChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
