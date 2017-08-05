package com.kirik.zen.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirik.zen.core.Zen;

public class BansConfiguration {
	
	private File playerData;
	private FileConfiguration playerDataConfig;	
	private Zen plugin = Zen.instance;
	
	public BansConfiguration(){
		playerData = new File(plugin.getDataFolder(), "bans.yml");
		playerDataConfig = YamlConfiguration.loadConfiguration(playerData);
	}
	
	public void createBansConfig(){
		try {
			playerData.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void createBansDefaults(){
		if(playerData.length() <= 0) {
			playerDataConfig.set("test", true);
		}
	}
	
	public FileConfiguration getBansConfig(){	
		return playerDataConfig;
	}
	
	public void saveBansConfig(){
		try {
			getBansConfig().save(playerData);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
