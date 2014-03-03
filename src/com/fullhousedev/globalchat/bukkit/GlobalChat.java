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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.util.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Austin
 */
public class GlobalChat extends JavaPlugin {

    public HashMap<String, String> players;
    public String serverName;
    public ArrayList<String> noMessagePlayers;
    public ArrayList<String> socialspyPlayers;

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this,
                "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this,
                "BungeeCord", new MessageListener(this));
        players = new HashMap<>();
        noMessagePlayers = new ArrayList<>();
        socialspyPlayers = new ArrayList<>();

        getServerName();
        
        //Runs the sync task later, because we need the server name before we
        //do anything else.
        new BukkitRunnable() {
            @Override
            public void run() {
                syncUsers();
            }
        }.runTaskLater(this, 20);
        
    }

    private void getServerName() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("GetServer");
        } catch (IOException ex) {
            Logger.getLogger(GlobalChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        // OR, if you don't need to send it to a specific player
        Player p = Bukkit.getOnlinePlayers()[0];

        p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
    }
    
    private void syncUsers() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("UserSync");
            
            ByteArrayOutputStream customData = new ByteArrayOutputStream();
            DataOutputStream outCustom = new DataOutputStream(customData);
            outCustom.writeUTF(serverName);
            
            out.writeShort(customData.toByteArray().length);
            out.write(customData.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(GlobalChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        // OR, if you don't need to send it to a specific player
        Player p = Bukkit.getOnlinePlayers()[0];

        p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
    }
    
    public void sendSyncResponse(String playerList) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("UserSync");
            
            ByteArrayOutputStream customData = new ByteArrayOutputStream();
            DataOutputStream outCustom = new DataOutputStream(customData);
            outCustom.writeUTF(serverName);
            outCustom.writeUTF(playerList);
            
            out.writeShort(customData.toByteArray().length);
            out.write(customData.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(GlobalChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        // OR, if you don't need to send it to a specific player
        Player p = Bukkit.getOnlinePlayers()[0];

        p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
    }

}
