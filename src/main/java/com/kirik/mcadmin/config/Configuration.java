package com.kirik.mcadmin.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirik.mcadmin.core.MCAdmin;

public class Configuration {
	
	private MCAdmin plugin;
	
	private FileConfiguration homeConfig = null;
	private File homeConfigFile = null;
	
	public Configuration(MCAdmin plugin){
		this.plugin = plugin;
		//homeConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "homes.yml"));
	}
	
	public void init(){
		plugin.saveDefaultConfig();
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
		getHomeConfig();
		saveHomeConfig();
	}
	
	public void reloadHomeConfig() {
		if(homeConfigFile == null){
			homeConfigFile = new File(plugin.getDataFolder(), "homes.yml");
		}
		homeConfig = YamlConfiguration.loadConfiguration(homeConfigFile);
	}
	
	public FileConfiguration getHomeConfig(){
		if(homeConfig == null){
			reloadHomeConfig();
		}
		return homeConfig;
	}
	
	public void saveHomeConfig(){
		if(homeConfig == null || homeConfigFile == null){
			return;
		}
		try {
			getHomeConfig().save(homeConfigFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
