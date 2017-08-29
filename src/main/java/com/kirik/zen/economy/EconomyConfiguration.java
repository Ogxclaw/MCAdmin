package com.kirik.zen.economy;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirik.zen.core.Zen;

public class EconomyConfiguration {
	
	private File economyData;
	private FileConfiguration economyDataConfig;	
	private Zen plugin = Zen.instance;
	
	public EconomyConfiguration(){
		economyData = new File(plugin.getDataFolder(), "eco.yml");
		economyDataConfig = YamlConfiguration.loadConfiguration(economyData);
	}
	
	public void createEcoConfig(){
		try {
			economyData.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void createEcoDefaults(){
		if(economyData.length() <= 0) {
			economyDataConfig.set("active", true);
		}
	}
	
	public FileConfiguration getEcoConfig(){	
		return economyDataConfig;
	}
	
	public void saveEcoConfig(){
		try {
			getEcoConfig().save(economyData);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
