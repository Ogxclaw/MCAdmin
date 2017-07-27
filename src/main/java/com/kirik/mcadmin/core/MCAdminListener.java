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
		Player player = event.getPlayer();
		//TODO Aggravating, but no better way to fix this at the moment. Unless I hard code ranks which is a big no-no
		event.setJoinMessage(ChatColor.DARK_GREEN + "[+]" + plugin.playerHelper.getPlayerPrefix(player).substring(0,2).replace('&', '\u00a7') + " " + player.getDisplayName() + ChatColor.YELLOW + " connected!");
		//MOTD
		//TODO Make this go to a file
		plugin.playerHelper.sendDirectedMessage(player, "Welcome!");
		plugin.playerHelper.sendDirectedMessage(player, "This is the message of the day!");
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
		Player player = event.getPlayer();
		if(plugin.playerHelper.getPersonalPlayerPrefix(player) != null){
			event.setFormat(plugin.playerHelper.getPersonalPlayerPrefix(player) + " " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
		}else{
			event.setFormat(plugin.playerHelper.getPlayerPrefix(player) + " " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
		}
	}
	
	@Override
	public void run(){}

}
