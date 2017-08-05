package com.kirik.mcadmin.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.config.PlayerConfiguration;
import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.main.StateContainer;
import com.kirik.mcadmin.main.offlinebukkit.OfflinePlayer;

public class PlayerHelper extends StateContainer {
	
	public MCAdmin plugin;
	
	public PlayerHelper(MCAdmin plugin){
		this.plugin = plugin;
	}
	
	public Player getPlayerByUUID(UUID uuid){
		for(Player p : plugin.getServer().getOnlinePlayers())
			if(p.getUniqueId().equals(uuid))
				return p;
		throw new IllegalArgumentException();
	}
	
	private Player literalMatch(String name){
		Player onlinePlayer = plugin.getServer().getPlayer(name);
		if(onlinePlayer != null)
			return onlinePlayer;
		
		return new OfflinePlayer(plugin.getServer(), name);
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
	
	public void setPlayerSuffix(Player player, String suffix){
		plugin.chat.setPlayerSuffix(player, suffix);
	}
	
	public String getPlayerSuffix(Player player){
		return plugin.chat.getGroupSuffix(player.getWorld(), this.getPlayerRank(player));
	}
	
	public String getPersonalPlayerSuffix(Player player){
		return plugin.chat.getPlayerSuffix(player);
	}
	
	//ranks
	public String getPlayerRank(Player player){
		return plugin.permission.getPrimaryGroup(player);
	}
	
	public void setPlayerRank(Player player, String rankName){
		PlayerConfiguration rank = new PlayerConfiguration(player.getUniqueId());
		plugin.permission.playerRemoveGroup(player, this.getPlayerRank(player));
		plugin.permission.playerAddGroup(player, rankName);
		
		rank.getPlayerConfig().set("rank", rankName);	
		rank.getPlayerConfig().set("level", getPlayerLevel(player));
		
		rank.savePlayerConfig();
	}
	
	//level
	public int getPlayerLevel(Player player){
		String rank = this.getPlayerRank(player);
		return getLevelOfRank(rank);
	}
	
	public int getLevelOfRank(String rankName){
		switch(rankName){
		case "member":
			return 10;
		case "helper":
			return 20;
		case "mod":
			return 30;
		case "admin":
			return 40;
		case "manager":
			return 50;
		case "kirik":
			return 666;
		default:
			return 10;
		}
	}
	
	/*public int getPlayerLevel(Player player){
		return plugin.config.getPlayerConfig(player.getUniqueId()).getInt("level");
	}*/
	
	//homes
	public Location getHome(Player player){
		PlayerConfiguration home = new PlayerConfiguration(player.getUniqueId());
		return (Location) home.getPlayerConfig().get("home");
	}
	
	public void setHome(Player player){
		PlayerConfiguration home = new PlayerConfiguration(player.getUniqueId());
		Location loc = player.getLocation();
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		home.getPlayerConfig().set("home", loc);
		home.savePlayerConfig();
	}
	
	public static final HashMap<UUID, String> playerHosts = new HashMap<>();
	public static final HashMap<UUID, String> playerIPs = new HashMap<>();
	
	public String getPlayerIP(Player player){
		return player.getAddress().getHostString();
	}
	
	public String getPlayerIP(UUID uuid){
		synchronized(PlayerHelper.playerIPs){
			return playerIPs.get(uuid);
		}
	}
	
	public String getPlayerHost(Player player){
		return player.getAddress().toString();
	}
	
	public String getPlayerHost(UUID uuid){
		synchronized(PlayerHelper.playerIPs){
			return playerHosts.get(uuid);
		}
	}
	
	//freeze
	public void setPlayerFrozen(Player player, boolean flag){
		PlayerConfiguration config = new PlayerConfiguration(player.getUniqueId());
		config.getPlayerConfig().set("frozen", flag);
		config.savePlayerConfig();
	}
	
	public boolean isFrozen(Player player){
		PlayerConfiguration config = new PlayerConfiguration(player.getUniqueId());
		return config.getPlayerConfig().getBoolean("frozen");
	}
}
