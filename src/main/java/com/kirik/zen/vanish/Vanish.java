package com.kirik.zen.vanish;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.entity.Player;

import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.core.Zen;

public class Vanish {
	
	private final Zen plugin;
	
	public Vanish(Zen plugin){
		this.plugin = plugin;
	}
	
	public Collection<Player> getAllInvisibleOnlinePlayers(){
		Collection<Player> onlineInvisiblePlayers = new HashSet<>();
		for(Player player : plugin.getServer().getOnlinePlayers()){
			PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
			if(playerConfig.getPlayerConfig().getBoolean("isVanished"))
				onlineInvisiblePlayers.add(player);
		}
		return onlineInvisiblePlayers;
	}
	
	public void hidePlayer(Player player){
		for(Player allPlayers : plugin.getServer().getOnlinePlayers()){
			if(!(allPlayers.hasPermission("zen.admin.vanish.show"))){
				allPlayers.hidePlayer(player);
			}
		}
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		playerConfig.getPlayerConfig().set("isVanished", true);
		playerConfig.savePlayerConfig();
	}
	
	public void showPlayer(Player player){
		for(Player allPlayers : plugin.getServer().getOnlinePlayers()){
			allPlayers.showPlayer(player);
		}
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		playerConfig.getPlayerConfig().set("isVanished", false);
		playerConfig.savePlayerConfig();
	}
}
