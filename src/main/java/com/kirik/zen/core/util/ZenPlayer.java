package com.kirik.zen.core.util;

import java.util.UUID;

import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.core.Zen;

//TODO UNUSED
public class ZenPlayer {
	
	private final String name;
	private String displayName;
	private final UUID uuid;
	
	static Zen plugin = Zen.instance;
	static PlayerHelper playerHelper = new PlayerHelper(plugin);
	
	public ZenPlayer(UUID uuid) {
		this(uuid, playerHelper.getPlayerByUUID(uuid).getName());
	}
	
	public ZenPlayer(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}
	
	public String getName() {
		/*PlayerConfiguration playerConfig = new PlayerConfiguration(uuid);
		return playerConfig.getPlayerConfig().getString("name");*/
		return name;
	}
	
	public void setName(String name) {
		PlayerConfiguration playerConfig = new PlayerConfiguration(uuid);
		playerConfig.getPlayerConfig().set("name", name);
		playerConfig.savePlayerConfig();
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
