package com.kirik.zen.main.listeners;

import java.io.File;
import java.util.Collection;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.kirik.zen.config.PlayerConfiguration;

import net.redstoneore.legacyfactions.Relation;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.FPlayerColl;

public class ZenPlayerListener extends BaseListener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		
		/*plugin.config.getPlayerConfig(player);
		plugin.config.savePlayerConfig(player);*/
		/*plugin.config.getPlayerConfig(player.getUniqueId());*/
		
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		
		
		File file = new File(plugin.getDataFolder() + "/players/", player.getUniqueId() + ".yml");
		if(!file.exists()) {
			Date currentDate = new Date(System.currentTimeMillis());
			playerConfig.createPlayerConfig();
			playerConfig.createPlayerDefaults();
			playerConfig.getPlayerConfig().set("joinDate", currentDate);
			playerConfig.savePlayerConfig();
		}
		
		plugin.logToConsole("Config loaded for player " + player.getName());
	
		plugin.getUUIDConfig().set(player.getName().toLowerCase() + ".uuid", player.getUniqueId().toString());
		plugin.saveUUIDConfig();
		plugin.logToConsole("UUID logged for player " + player.getName());
		
		
		if(playerConfig.getPlayerConfig().getString("rank") == null){
			playerHelper.setPlayerRank(player, "member"); //TODO find default group / set it manually.
			plugin.logToConsole(player.getName() + " has logged in with no rank and given member.");
		}
		//TODO Aggravating, but no better way to fix this at the moment. Unless I hard code ranks which is a big no-no
		event.setJoinMessage(ChatColor.DARK_GREEN + "[+]" + plugin.playerHelper.getPlayerPrefix(player).replace('&', '\u00a7') + " " + player.getDisplayName() + ChatColor.YELLOW + " connected!");
		plugin.playerHelper.sendDirectedMessage(player, "Welcome to the \u00a76Zenium Server Network\u00a7f!");
		plugin.playerHelper.sendDirectedMessage(player, "We are currently in beta, so there's much more fun to come!");
		/*plugin.playerHelper.sendDirectedMessage(player, plugin.getConfig().getString("motd").replaceAll("%p", player.getName()));*/
	}
	
	@EventHandler
	public void onPlayerMovement(PlayerMoveEvent event){
		Player player = event.getPlayer();
		if(playerHelper.isFrozen(player)){
			event.setCancelled(true);
			playerHelper.sendDirectedMessage(player, "You are frozen!");
		}
		/*if(playerHelper.hasPlayerTpTimer(player)){
			playerHelper.setPlayerTpTimer(player, false);
			playerHelper.sendDirectedMessage(player, "Teleportation cancelled!");
		}*/
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
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		ZenOnPlayerChat(event);
		event.setCancelled(true);
	}
	
	private void ZenOnPlayerChat(AsyncPlayerChatEvent event){
		//TODO Just make a big ass hook class you fucking imbred.
		Player player = event.getPlayer();
		//TODO Factions integration
		FPlayer mPlayer = FPlayerColl.get(player);
		Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
		
		//MUTE
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());	
		boolean isMuted = playerConfig.getPlayerConfig().getBoolean("isMuted");
		if(isMuted){
			playerHelper.sendDirectedMessage(player, "You are muted! You will not be heard.");
			return;
		}
		
		
		//OPCHAT
		if(event.getMessage().startsWith("#")){
			String msg = event.getMessage().substring(1, event.getMessage().length());
			for(Player allPlayers : players){
				if(allPlayers.hasPermission("zen.opchat.visible") || allPlayers.equals(player)){
					playerHelper.sendChatMessage(allPlayers, ChatColor.YELLOW + "[OPCHAT] " + plugin.playerHelper.getPersonalPlayerPrefix(player).replace('&', '\u00a7') + player.getDisplayName() + ChatColor.WHITE + ": " + ChatColor.RESET + msg);
				}
			}
			return;
		}
		
		for(Player allPlayers : players){
			if(mPlayer.hasFaction()){
				FPlayer mAllPlayers = FPlayerColl.get(allPlayers);
				ChatColor col = ChatColor.GOLD;
				if(mPlayer.getFaction().getRelationTo(mAllPlayers.getFaction()) == Relation.ALLY)
					col = ChatColor.GREEN;
				else if(mPlayer.getFaction().getRelationTo(mAllPlayers.getFaction()) == Relation.ENEMY)
					col = ChatColor.RED;
				else if(mPlayer.getFaction().getRelationTo(mAllPlayers.getFaction()) == Relation.TRUCE)
					col = ChatColor.AQUA;
				else if(mPlayer.getFaction().getRelationTo(mAllPlayers.getFaction()) == Relation.MEMBER)
					col = ChatColor.DARK_GRAY;
				if(playerHelper.getPlayerSuffix(player) != null){
					playerHelper.sendChatMessage(allPlayers, col + mPlayer.getFaction().getTag() + " " + plugin.playerHelper.getPersonalPlayerPrefix(player).replace('&', '\u00a7') + player.getDisplayName() + playerHelper.getPersonalPlayerSuffix(player) + ChatColor.WHITE + ": " + event.getMessage().replace('&', '\u00a7'));
				}else{
					playerHelper.sendChatMessage(allPlayers, col + mPlayer.getFaction().getTag() + " " + plugin.playerHelper.getPersonalPlayerPrefix(player).replace('&', '\u00a7') + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage().replace('&', '\u00a7'));
				}
			}else{
				if(playerHelper.getPlayerSuffix(player) != null){
					playerHelper.sendChatMessage(allPlayers, plugin.playerHelper.getPersonalPlayerPrefix(player).replace('&', '\u00a7') + player.getDisplayName() + playerHelper.getPersonalPlayerSuffix(player) + ChatColor.WHITE + ": " + event.getMessage().replace('&', '\u00a7'));
				}else{
					playerHelper.sendChatMessage(allPlayers, plugin.playerHelper.getPersonalPlayerPrefix(player).replace('&', '\u00a7') + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage().replace('&', '\u00a7'));
				}
			}
		}
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
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event){
		final Player player = event.getPlayer();
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		playerConfig.getPlayerConfig().set("lastLogoutIP", playerHelper.getPlayerIP(player));
		playerConfig.savePlayerConfig();
	}
}
