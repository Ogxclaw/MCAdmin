package com.kirik.zen.core.util;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.core.Zen;

//TODO UNUSED
//TODO same issue I used to have with homes. need to have a unique uuid every time this is called.
public class ZenPlayer {
	
	private final String name;
	//private String displayName;
	private final UUID uuid;
	private PlayerConfiguration _playerConfig;
	private FileConfiguration playerConfig;
	
	static Zen plugin = Zen.instance;
	static PlayerHelper playerHelper = new PlayerHelper(plugin);
	
	public ZenPlayer(Player player) {
		this(player.getUniqueId(), player.getName());
	}
	
	public ZenPlayer(UUID uuid) {
		this(uuid, playerHelper.getPlayerByUUID(uuid).getName());
	}
	
	public ZenPlayer(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
		this._playerConfig = new PlayerConfiguration(uuid);
		this.playerConfig = _playerConfig.getPlayerConfig();
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getName() {
		if(name != null)
			return name;
		else
			return playerConfig.getString("realName");
	}
	
	public void setDisplayName(String name) {
		playerConfig.set("displayName", name);
		this.savePlayer(); 
	}
	
	public String getDisplayName() {
		return playerConfig.getString("displayName");
	}
	
	public void savePlayer() {
		_playerConfig.savePlayerConfig();
	}
}
