package com.kirik.mcadmin.core;

import java.util.logging.Level;

import org.bukkit.craftbukkit.v1_12_R1.command.ColouredConsoleSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kirik.mcadmin.commands.system.CommandSystem;
import com.kirik.mcadmin.componentsystem.ComponentSystem;
import com.kirik.mcadmin.config.Configuration;
import com.kirik.mcadmin.core.util.PlayerHelper;
import com.kirik.mcadmin.main.StateContainer;
import com.kirik.mcadmin.main.listeners.MCAdminPlayerListener;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class MCAdmin extends JavaPlugin {
	
	public static MCAdmin instance;
	public PlayerHelper playerHelper;
	public CommandSystem commandSystem;
	public ComponentSystem componentSystem = new ComponentSystem();
	
	public Configuration config;
	
	private MCAdminPlayerListener listener;
	
	//VAULT
	public Permission permission = null;
	public Chat chat = null;
	
	public MCAdmin(){
		instance = this;
		componentSystem.registerComponents();
	}
	
	@Override
	public void onEnable(){
		playerHelper = new PlayerHelper(this);
		
		StateContainer.loadAll();
		
		config = new Configuration(this);
		config.init();
		logToConsole("Configuration loaded.");
		
		setupPermissions();
		setupChat();
		logToConsole("Vault Hooked.");
		
		new MCAdminPlayerListener();
		componentSystem.registerListeners();
		logToConsole("Listeners loaded.");
		
		commandSystem = new CommandSystem(this);
		componentSystem.registerCommands();
		logToConsole("Commands loaded.");
	}
	
	private boolean setupPermissions(){
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if(permissionProvider != null){
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}
	
	private boolean setupChat(){
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
        return (chat != null);
	}
	
	public MCAdminPlayerListener getPlayerListener(){
		return listener;
	}
	
	@Override
	public void onDisable(){
		saveConfig();
		log("[MCAdmin] Shutting down...");
	}
	
	public void log(String msg){
		log(Level.INFO, msg);
	}
	
	public void logToConsole(String msg){
		ColouredConsoleSender.getInstance().sendMessage(ChatColor.DARK_PURPLE + "[MCAdmin] " + ChatColor.WHITE + msg);
	}
	
	public void logErrorToConsole(String msg){
		ColouredConsoleSender.getInstance().sendMessage(ChatColor.DARK_RED + "[MCAdmin] " + ChatColor.WHITE + msg);
	}
	
	public void log(Level level, String msg){
		getLogger().log(level, msg);
	}

}
