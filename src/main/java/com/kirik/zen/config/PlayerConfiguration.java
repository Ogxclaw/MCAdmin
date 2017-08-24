package com.kirik.zen.config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirik.zen.core.Zen;

public class PlayerConfiguration {
	
	private UUID uuid;
	private File playerData;
	private FileConfiguration playerDataConfig;	
	private Zen plugin = Zen.instance;
	
	public PlayerConfiguration(UUID uuid){
		this.setUuid(uuid);
		
		playerData = new File(plugin.getDataFolder() + "/players/", uuid + ".yml");
		playerDataConfig = YamlConfiguration.loadConfiguration(playerData);
	}
	
	public void createPlayerConfig(){
		try {
			playerData.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void createPlayerDefaults(){
		if(playerData.length() <= 0) {
			playerDataConfig.set("test", true);;
		}
	}
	
	public FileConfiguration getPlayerConfig(){	
		return playerDataConfig;
	}
	
	public void savePlayerConfig(){
		try {
			getPlayerConfig().save(playerData);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

}
