package com.kirik.zen.warps;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirik.zen.core.Zen;

public class WarpsConfiguration {
	
	private File warpData;
	private FileConfiguration warpDataConfig;	
	private Zen plugin = Zen.instance;
	
	public WarpsConfiguration(){
		warpData = new File(plugin.getDataFolder(), "warps.yml");
		warpDataConfig = YamlConfiguration.loadConfiguration(warpData);
	}
	
	public void createWarpsConfig(){
		try {
			warpData.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void createWarpsDefaults(){
		if(warpData.length() <= 0) {
			warpDataConfig.set("test", true);
		}
	}
	
	public FileConfiguration getWarpsConfig(){	
		return warpDataConfig;
	}
	
	public void saveWarpsConfig(){
		try {
			getWarpsConfig().save(warpData);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
