package com.kirik.zen.core;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.command.ColouredConsoleSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.kirik.zen.chat.listeners.SuffixListener;
import com.kirik.zen.commands.system.CommandSystem;
import com.kirik.zen.componentsystem.ComponentSystem;
import com.kirik.zen.config.BansConfiguration;
import com.kirik.zen.config.UUIDConfiguration;
import com.kirik.zen.core.util.PlayerHelper;
import com.kirik.zen.economy.Economy;
import com.kirik.zen.economy.EconomyConfiguration;
import com.kirik.zen.economy.listener.EconomyListener;
import com.kirik.zen.fun.SuffixConfiguration;
import com.kirik.zen.main.SignMenu;
import com.kirik.zen.main.StateContainer;
import com.kirik.zen.main.console.ZenConsoleCommands;
import com.kirik.zen.main.listeners.InventoryListener;
import com.kirik.zen.main.listeners.ZenPlayerListener;
import com.kirik.zen.vanish.Vanish;
import com.kirik.zen.warps.WarpsConfiguration;
import com.sk89q.worldedit.WorldEdit;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.redstoneore.legacyfactions.Factions;

//Zenium Server Network
public class Zen extends JavaPlugin {
	
	public static Zen instance;
	public PlayerHelper playerHelper;
	public Economy eco;
	public CommandSystem commandSystem;
	public ComponentSystem componentSystem = new ComponentSystem();
	
	private ZenPlayerListener listener;
	
	private BansConfiguration bansConfig;
	private UUIDConfiguration uuidConfig;
	private WarpsConfiguration warpsConfig;
	private EconomyConfiguration ecoConfig;
	private SuffixConfiguration suffixConfig;
	
	public Vanish vanish = new Vanish(this);
	
	public SignMenu signMenu;
	
	//VAULT
	public Permission permission = null;
	public Chat chat = null;
	
	//FACTIONS
	public Factions factions = null;
	
	//WORLDEDIT
	public WorldEdit worldEdit = null;
	
	
	//public Date currentDate;
	
	public Zen(){
		instance = this;
		componentSystem.registerComponents();
	}
	
	@Override
	public void onEnable(){
		//currentDate = new Date(System.currentTimeMillis());
		
		playerHelper = new PlayerHelper(this);
		eco = new Economy(this);
		
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
		
		warpsConfig = new WarpsConfiguration();
		warpsConfig.createWarpsConfig();
		warpsConfig.createWarpsDefaults();
		this.saveWarpsConfig();
		
		ecoConfig = new EconomyConfiguration();
		ecoConfig.createEcoConfig();
		ecoConfig.createEcoDefaults();
		this.saveEcoConfig();
		
		suffixConfig = new SuffixConfiguration();
		suffixConfig.createSuffixConfig();
		suffixConfig.createSuffixDefaults();
		this.saveSuffixConfig();
		
		setupWorldEdit();
		logToConsole("WorldEdit Hooked.");
		
		setupFactions();
		logToConsole("Factions Hooked.");
		
		setupPermissions();
		setupChat();
		logToConsole("Vault Hooked.");
		
		signMenu = new SignMenu(this);
		
		//new CombatListener();
		//new FactionsHookListener();
		new ZenPlayerListener();
		new InventoryListener();
		new EconomyListener();
		new SuffixListener();
		componentSystem.registerListeners();
		logToConsole("Listeners loaded.");
		
		commandSystem = new CommandSystem(this);
		componentSystem.registerCommands();
		logToConsole("Commands loaded.");
		
		new ZenConsoleCommands(this);
	}
	
	private boolean setupWorldEdit(){
		RegisteredServiceProvider<WorldEdit> worldEditProvider = getServer().getServicesManager().getRegistration(com.sk89q.worldedit.WorldEdit.class);
		if(worldEditProvider != null){
			worldEdit = worldEditProvider.getProvider();
		}
		return (worldEdit != null);
	}
	
	private boolean setupFactions(){
		RegisteredServiceProvider<Factions> factionsProvider = getServer().getServicesManager().getRegistration(net.redstoneore.legacyfactions.Factions.class);
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
	public FileConfiguration getSuffixConfig() {
		return suffixConfig.getSuffixConfig();
	}
	
	public void saveSuffixConfig() {
		suffixConfig.saveSuffixConfig();
	}
	
	public FileConfiguration getEcoConfig(){
		return ecoConfig.getEcoConfig();
	}
	
	public void saveEcoConfig(){
		ecoConfig.saveEcoConfig();
	}
	
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
	
	public FileConfiguration getWarpsConfig(){
		return warpsConfig.getWarpsConfig();
	}
	
	public void saveWarpsConfig(){
		warpsConfig.saveWarpsConfig();
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
