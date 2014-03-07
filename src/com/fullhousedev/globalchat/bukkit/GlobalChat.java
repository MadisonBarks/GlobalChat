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

import com.fullhousedev.globalchat.bukkit.commands.GChatListener;
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
    public ArrayList<String> toggledUsers;
    public ArrayList<String> socialspyUsers;

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this,
                "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this,
                "BungeeCord", new MessageListener(this));
        players = new HashMap<>();
        toggledUsers = new ArrayList<>();
        socialspyUsers = new ArrayList<>();

        PluginMessageManager.getServerName(this);
        
        getCommand("gchat").setExecutor(new GChatListener(this));
        getServer().getPluginManager().registerEvents(new EventListeners(this),
                this);
    }

}
