package com.kirik.zen.fun;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kirik.zen.core.Zen;

@Deprecated
public class SuffixConfiguration {
	
	private File suffixData;
	private FileConfiguration suffixDataConfig;	
	private Zen plugin = Zen.instance;
	
	public SuffixConfiguration(){
		suffixData = new File(plugin.getDataFolder(), "suffixes.yml");
		suffixDataConfig = YamlConfiguration.loadConfiguration(suffixData);
	}
	
	public void createSuffixConfig(){
		try {
			suffixData.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void createSuffixDefaults(){
		if(suffixData.length() <= 0) {
			suffixDataConfig.set("test", true);
		}
	}
	
	public FileConfiguration getSuffixConfig(){	
		return suffixDataConfig;
	}
	
	public void saveSuffixConfig(){
		try {
			getSuffixConfig().save(suffixData);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
