package com.kirik.mcadmin.main.listeners;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.kirik.mcadmin.config.PlayerConfiguration;
import com.kirik.mcadmin.config.UUIDConfiguration;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

public class MCAdminPlayerListener extends BaseListener {
	
	FileConfiguration playerConfig = null;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		
		/*plugin.config.getPlayerConfig(player);
		plugin.config.savePlayerConfig(player);*/
		/*plugin.config.getPlayerConfig(player.getUniqueId());*/
		
		
		PlayerConfiguration config = new PlayerConfiguration(player.getUniqueId());
		config.createPlayerConfig();
		config.createPlayerDefaults();
		config.savePlayerConfig();
		plugin.logToConsole("Config loaded for player " + player.getName());
		
		UUIDConfiguration uuidConfig = new UUIDConfiguration();
		uuidConfig.createUUIDConfig();
		uuidConfig.createUUIDDefaults();
		uuidConfig.getUUIDConfig().set(player.getName().toLowerCase() + ".uuid", player.getUniqueId().toString());
		uuidConfig.saveUUIDConfig();
		plugin.logToConsole("UUID logged for player " + player.getName());
		
		if(plugin.permission.getPlayerGroups(player) == null){
			playerHelper.setPlayerRank(player, "member"); //TODO find default group / set it manually.
			plugin.logToConsole(player.getName() + " has logged in with no rank and given member.");
		}
		//TODO Aggravating, but no better way to fix this at the moment. Unless I hard code ranks which is a big no-no
		event.setJoinMessage(ChatColor.DARK_GREEN + "[+]" + plugin.playerHelper.getPlayerPrefix(player).substring(0,2).replace('&', '\u00a7') + " " + player.getDisplayName() + ChatColor.YELLOW + " connected!");
		//TODO Make this go to a file
		plugin.playerHelper.sendDirectedMessage(player, plugin.getConfig().getString("motd").replaceAll("%p", player.getName()));
	}
	
	@EventHandler
	public void onPlayerMovement(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(playerHelper.isFrozen(player)){
			event.setCancelled(true);
			playerHelper.sendDirectedMessage(player, "You are frozen!");
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		event.setQuitMessage(ChatColor.DARK_RED + "[-]" + plugin.playerHelper.getPlayerPrefix(player).substring(0,2).replace('&', '\u00a7') + " " + player.getDisplayName() + ChatColor.YELLOW + " disconnected!");
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		event.setLeaveMessage(ChatColor.DARK_RED + "[-]" + plugin.playerHelper.getPlayerPrefix(player).substring(0,2).replace('&', '\u00a7') + " " + player.getDisplayName() + ChatColor.YELLOW + " was kicked! ((" + event.getReason() + "))");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		//TODO Just make a big ass hook class you fucking imbred.
		Player player = event.getPlayer();
		//TODO Factions integration
		MPlayer mPlayer = MPlayer.get(player.getUniqueId());
		Faction faction = mPlayer.getFaction();
		/*if(plugin.playerHelper.getPersonalPlayerPrefix(player) != null){
			event.setFormat(plugin.playerHelper.getPersonalPlayerPrefix(player).replace('&', '\u00a7') + " " + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage().replace('&', '\u00a7'));
		}else{*/
		if(!mPlayer.hasFaction()){
			event.setFormat(plugin.playerHelper.getPlayerPrefix(player).replace('&', '\u00a7') + /*" " +*/ player.getDisplayName() + playerHelper.getPersonalPlayerSuffix(player) + ChatColor.WHITE + ": " + event.getMessage().replace('&', '\u00a7'));
		}else{
			event.setFormat(ChatColor.GOLD + faction.getName() + " " + ChatColor.RESET + plugin.playerHelper.getPlayerPrefix(player).replace('&', '\u00a7') + /*" " +*/ player.getDisplayName() + playerHelper.getPersonalPlayerSuffix(player) + ChatColor.WHITE + ": " + event.getMessage().replace('&', '\u00a7'));
		}
		//}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPreprocessCommand(PlayerCommandPreprocessEvent event){
		final Player player = event.getPlayer();
		final String cmdString = event.getMessage().substring(1).trim();
		
		if(plugin.commandSystem.runCommand(player, cmdString)){
			event.setCancelled(true);
			event.setMessage("/youdontwantthiscommand " + event.getMessage());
		}else{
			plugin.logToConsole("Other Command: " + player.getName() + ": " + cmdString);
		}
	}
}
