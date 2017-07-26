package com.kirik.mcadmin.core;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MCAdminListener implements Listener, Runnable {
	
	final MCAdmin plugin;
	
	public MCAdminListener(MCAdmin plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		event.setJoinMessage(ChatColor.DARK_GREEN + "[+] " + ChatColor.RESET + event.getPlayer().getDisplayName() + ChatColor.YELLOW + " connected!");
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		event.setQuitMessage(playerDisconnect(event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		event.setLeaveMessage(playerDisconnect(event.getPlayer()));
	}
	
	private String playerDisconnect(Player player){
		return ChatColor.DARK_RED + "[-] " + ChatColor.RESET + player.getDisplayName() + ChatColor.YELLOW + " disconnected!";
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		//TODO Just make a big ass hook class you fucking imbred.
		String prefix = plugin.chat.getGroupPrefix(plugin.getServer().getWorld("world"), plugin.permission.getPrimaryGroup(event.getPlayer()));
		event.setFormat(prefix + event.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
	}
	
	@Override
	public void run(){}

}
