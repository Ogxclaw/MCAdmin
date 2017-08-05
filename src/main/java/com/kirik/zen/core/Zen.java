package com.kirik.zen.core;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_12_R1.command.ColouredConsoleSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kirik.zen.commands.system.CommandSystem;
import com.kirik.zen.componentsystem.ComponentSystem;
import com.kirik.zen.config.BansConfiguration;
import com.kirik.zen.core.util.PlayerHelper;
import com.kirik.zen.factions.listeners.FactionsHookListener;
import com.kirik.zen.main.StateContainer;
import com.kirik.zen.main.console.ZenConsoleCommands;
import com.kirik.zen.main.listeners.ZenPlayerListener;
import com.massivecraft.factions.Factions;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

//TODO Refractor to [Zen] - Zenium Server Network
public class Zen extends JavaPlugin {
	
	public static Zen instance;
	public PlayerHelper playerHelper;
	public CommandSystem commandSystem;
	public ComponentSystem componentSystem = new ComponentSystem();
	
	private ZenPlayerListener listener;
	
	//VAULT
	public Permission permission = null;
	public Chat chat = null;
	
	//FACTIONS
	private Factions fac;
	
	public Zen(){
		instance = this;
		componentSystem.registerComponents();
	}
	
	@Override
	public void onEnable(){
		playerHelper = new PlayerHelper(this);
		
		checkPlugins();
		
		StateContainer.loadAll();
		
		BansConfiguration bans = new BansConfiguration();
		bans.createBansConfig();
		bans.createBansDefaults();
		bans.saveBansConfig();
		
		setupPermissions();
		setupChat();
		logToConsole("Vault Hooked.");
		
		new FactionsHookListener();
		new ZenPlayerListener();
		componentSystem.registerListeners();
		logToConsole("Listeners loaded.");
		
		commandSystem = new CommandSystem(this);
		componentSystem.registerCommands();
		logToConsole("Commands loaded.");
		
		new ZenConsoleCommands(this);
	}
	
	public void checkPlugins(){
		if(Bukkit.getPluginManager().getPlugin("Factions") != null){
			this.fac = (Factions)Bukkit.getPluginManager().getPlugin("Factions");
			logToConsole("Factions Hooked");
		}else{
			logErrorToConsole("Factions did not load!");
		}
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
	
	public ZenPlayerListener getPlayerListener(){
		return listener;
	}
	
	@Override
	public void onDisable(){
		saveConfig();
		log("[Zen] Shutting down...");
	}
	
	public void log(String msg){
		log(Level.INFO, msg);
	}
	
	public void logToConsole(String msg){
		ColouredConsoleSender.getInstance().sendMessage(ChatColor.DARK_PURPLE + "[Zen] " + ChatColor.WHITE + msg);
	}
	
	public void logErrorToConsole(String msg){
		ColouredConsoleSender.getInstance().sendMessage(ChatColor.DARK_RED + "[Zen] " + ChatColor.WHITE + msg);
	}
	
	public void log(Level level, String msg){
		getLogger().log(level, msg);
	}
	
	public World getOrCreateWorld(String name, Environment env){
		name = name.toLowerCase();
		World world = getServer().getWorld(name);
		if(world == null)
			return  getServer().createWorld(WorldCreator.name(name).environment(env));
		return world;
	}

}
