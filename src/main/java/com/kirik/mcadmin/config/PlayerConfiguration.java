package com.kirik.mcadmin.config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirik.mcadmin.core.MCAdmin;

public class PlayerConfiguration {
	
	private UUID uuid;
	private File playerData;
	private FileConfiguration playerDataConfig;	
	private MCAdmin plugin = MCAdmin.instance;
	
	public PlayerConfiguration(UUID uuid){
		this.uuid = uuid;
		
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

}
