package com.kirik.mcadmin.core.util;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.main.StateContainer;
import com.kirik.mcadmin.main.offlinebukkit.OfflinePlayer;

public class PlayerHelper extends StateContainer {
	
	public MCAdmin plugin;
	
	public PlayerHelper(MCAdmin plugin){
		this.plugin = plugin;
	}
	
	private Player literalMatch(String name){
		Player onlinePlayer = plugin.getServer().getPlayer(name);
		if(onlinePlayer != null)
			return onlinePlayer;
		
		return new OfflinePlayer(plugin.getServer(), name);
	}
	
	public static File getPlayerFile(String playerName, String world){
		File directory = new File(world + "/players/");
		
		if(!directory.exists())
			return null;
		
		if(!directory.isDirectory())
			return null;
		
		for(String file : directory.list()){
			if(!file.equalsIgnoreCase(playerName + ".dat"))
				continue;
			
			return new File(world + "/players/" + file);
		}
		return null;
	}
	
	public static File getPlayerFile(UUID playerUUID, String world){
		return new File(world+"/players/" +playerUUID.toString() + ".dat");
	}
	
	private static final Pattern quotePattern = Pattern.compile("^\"(.*)\"$");
	public Player matchPlayerSingle(String subString, boolean implicitlyLiteral) throws PlayerNotFoundException, MultiplePlayersFoundException {
		Matcher matcher = quotePattern.matcher(subString);
		
		if(matcher.matches())
			return literalMatch(matcher.group(1));
		
		List<Player> players = plugin.getServer().matchPlayer(subString);
		
		int c = players.size();
		if(c < 1)
			if(implicitlyLiteral)
				return literalMatch(subString);
			else
				throw new PlayerNotFoundException();
		if(c > 1)
			throw new MultiplePlayersFoundException(players);
		
		return players.get(0);
	}
	
	public Player matchPlayerSingle(String subString) throws PlayerNotFoundException, MultiplePlayersFoundException {
		return matchPlayerSingle(subString, false);
	}
	
	public String completePlayerName(String subString, boolean implicitlyLiteralNames){
		Matcher matcher = quotePattern.matcher(subString);
		
		if(matcher.matches())
			return matcher.group(1);
		
		List<Player> otherPlayers = plugin.getServer().matchPlayer(subString);
		int c = otherPlayers.size();
		
		if(c == 0 && implicitlyLiteralNames)
			return subString;
		
		if(c == 1)
			return otherPlayers.get(0).getName();
		
		return null;
	}
	
	//message sending
	public void sendServerMessage(String msg){
		sendServerMessage(msg, '5');
	}
	
	public void sendServerMessage(String msg, char colorCode){
		msg = "\u00a7" + colorCode + "[MCAdmin]\u00a7f " + msg;
		
		Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
		
		for(Player player : players){
			player.sendMessage(msg);
		}
	}
	
	public void sendDirectedMessage(CommandSender commandSender, String s){
		s = "\u00a75" + "[MCAdmin] \u00a7f" + s;
		commandSender.sendMessage(s);
	}
	
	public static void sendDirectedMessage(CommandSender commandSender, String s, char c){
		s = "\u00a7" + c + "[MCAdmin] \u00a7f" + s;
		commandSender.sendMessage(s);
	}
	
	//prefix
	public String getPlayerPrefix(Player player){
		return plugin.chat.getGroupPrefix(player.getWorld(), this.getPlayerRank(player));
	}
	
	public String getPersonalPlayerPrefix(Player player){
		return plugin.chat.getPlayerPrefix(player);
	}
	
	public void setPlayerPrefix(Player player, String prefix){
		plugin.chat.setPlayerPrefix(player, prefix);
	}
	
	//ranks
	public String getPlayerRank(Player player){
		return plugin.permission.getPrimaryGroup(player);
	}
}
