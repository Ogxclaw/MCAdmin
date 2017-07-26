package com.kirik.mcadmin.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.offlinebukkit.OfflinePlayer;

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
	
	//TODO CHANGE FIRST STRING TO UUID
	private Hashtable<String, String> playernicks = new Hashtable<String, String>();
	@Loader({"nicks", "nick", "nicknames", "nickname", "nick_names", "nick_name"})
	public void loadPlayerNames(){
		playernicks.clear();
		try {
			BufferedReader stream = new BufferedReader(new FileReader("player-nicks.txt"));
			String line;
			int lpos;
			while((line = stream.readLine()) != null){
				lpos = line.lastIndexOf('=');
				playernicks.put(line.substring(0, lpos), line.substring(lpos+1));
			}
			stream.close();
		}catch(Exception e){}
	}
	
	@Saver({"nicks", "nick", "nicknames", "nickname", "nick_names", "nick_name"})
	public void savePlayerNicks(){
		try {
			BufferedWriter stream = new BufferedWriter(new FileWriter("player-nicks.txt"));
			Enumeration<String> e = playernicks.keys();
			while(e.hasMoreElements()){
				String key = e.nextElement();
				stream.write(key + "=" + playernicks.get(key));
				stream.newLine();
			}
			stream.close();
		}catch(Exception e){}
	}
	
	public String getPlayerNick(String name){
		name = name.toLowerCase();
		if(playernicks.containsKey(name))
			return playernicks.get(name);
		else
			return null;
	}
	
	public void setPlayerNick(String name, String tag){
		name = name.toLowerCase();
		if(tag == null){
			playernicks.remove(name);
		}else{
			playernicks.put(name, tag);
		}
		savePlayerNicks();
	}
	
	public void setPlayerDisplayName(Player player){
		String nick = getPlayerNick(player.getName());
		if(nick == null)
			nick = player.getName();
		player.setDisplayName(nick);
	}
	
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
	
	/*public void sendServerMessage(String msg){
		sendServerMessage(msg, permission, '5');
	}*/
	
	/*public void sendServerMessage(String msg, CommandSender... exceptPlayers){
		
	}*/
}
