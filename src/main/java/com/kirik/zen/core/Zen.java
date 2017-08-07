package com.kirik.zen.core;

import java.util.Date;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.command.ColouredConsoleSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kirik.zen.commands.system.CommandSystem;
import com.kirik.zen.componentsystem.ComponentSystem;
import com.kirik.zen.config.BansConfiguration;
import com.kirik.zen.config.UUIDConfiguration;
import com.kirik.zen.core.util.PlayerHelper;
import com.kirik.zen.factions.listeners.FactionsHookListener;
import com.kirik.zen.main.StateContainer;
import com.kirik.zen.main.console.ZenConsoleCommands;
import com.kirik.zen.main.listeners.ZenPlayerListener;
import com.massivecraft.factions.Factions;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

//Zenium Server Network
public class Zen extends JavaPlugin {
	
	public static Zen instance;
	public PlayerHelper playerHelper;
	public CommandSystem commandSystem;
	public ComponentSystem componentSystem = new ComponentSystem();
	
	private ZenPlayerListener listener;
	
	private BansConfiguration bansConfig;
	private UUIDConfiguration uuidConfig;
	
	//VAULT
	public Permission permission = null;
	public Chat chat = null;
	
	//FACTIONS
	public Factions factions = null;
	
	//public Date currentDate;
	
	public Zen(){
		instance = this;
		componentSystem.registerComponents();
	}
	
	@Override
	public void onEnable(){
		//currentDate = new Date(System.currentTimeMillis());
		
		playerHelper = new PlayerHelper(this);
		
		//checkPlugins();
		
		StateContainer.loadAll();
		
		bansConfig = new BansConfiguration();
		bansConfig.createBansConfig();
		bansConfig.createBansDefaults();
		this.saveBansConfig();
		
		uuidConfig = new UUIDConfiguration();
		uuidConfig.createUUIDConfig();
		uuidConfig.createUUIDDefaults();
		this.saveUUIDConfig();
		
		setupFactions();
		logToConsole("Factions Hooked.");
		
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
	
	//TODO Add worldedit so I can add jails
	
	//TODO jail, setjail, servertime, kickall, lockdown, unban, alt tracking, mute, muteall, butcher, clear, compass, fullbright, gamemode, leash, rage, speed
	//TODO advertisement, at, autoexec, bind, console, exec, god, heal, reloadconfig, rescan, restart, give, particle, spawnat, throw, teleport, back, noport, nosummon, notp,
	//TODO send, summon, tp, transmute, warp, setwarp, banish, setspawn, spawn, pm, custom scoreboard?, kits, buycraft
	
	private boolean setupFactions(){
		RegisteredServiceProvider<Factions> factionsProvider = getServer().getServicesManager().getRegistration(com.massivecraft.factions.Factions.class);
		if(factionsProvider != null){
			factions = factionsProvider.getProvider();
		}
		return (factions != null);
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
	
	//START CONFIGS
	public FileConfiguration getBansConfig(){
		return bansConfig.getBansConfig();
	}
	
	public void saveBansConfig(){
		bansConfig.saveBansConfig();
	}
	
	public FileConfiguration getUUIDConfig(){
		return uuidConfig.getUUIDConfig();
	}
	
	public void saveUUIDConfig(){
		uuidConfig.saveUUIDConfig();
	}
	
	//END CONFIGS
	
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
