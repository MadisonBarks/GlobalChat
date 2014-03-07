/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fullhousedev.globalchat.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author austin
 */
public class EventListeners implements Listener{
    
    private GlobalChat plugin;
    public static boolean waitingOnJoin = false;
    
    public EventListeners(GlobalChat pl) {
        plugin = pl;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje) {
        if(waitingOnJoin) {
            PluginMessageManager.getServerName(plugin);
            waitingOnJoin = false;
        }
        PluginMessageManager.userConnect(pje.getPlayer().getName().toLowerCase(),
                plugin.serverName, plugin);
    }
    
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent pqe) {
        PluginMessageManager.userDisconnect(pqe.getPlayer().getName().toLowerCase()
                , plugin);
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent pke) {
        PluginMessageManager.userDisconnect(pke.getPlayer().getName().toLowerCase()
                , plugin);
    }
}
