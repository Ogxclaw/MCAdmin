package com.kirik.zen.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirik.zen.core.Zen;

public class UUIDConfiguration {
	
	private File uuidData;
	private FileConfiguration uuidDataConfig;
	private Zen plugin = Zen.instance;
	
	public UUIDConfiguration(){
		uuidData = new File(plugin.getDataFolder(), "uuids.yml");
		uuidDataConfig = YamlConfiguration.loadConfiguration(uuidData);
	}
	
	public void createUUIDConfig(){
		try {
			uuidData.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void createUUIDDefaults(){
		if(uuidData.length() <= 0) {
			uuidDataConfig.set("test", true);;
		}
	}
	
	public FileConfiguration getUUIDConfig(){	
		return uuidDataConfig;
	}
	
	public void saveUUIDConfig(){
		try {
			getUUIDConfig().save(uuidData);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
