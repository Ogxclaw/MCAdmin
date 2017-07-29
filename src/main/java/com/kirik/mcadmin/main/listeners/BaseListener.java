package com.kirik.mcadmin.main.listeners;

import org.bukkit.event.Listener;

import com.kirik.mcadmin.componentsystem.MCAdminListener;
import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.core.util.PlayerHelper;

public abstract class BaseListener implements Listener, MCAdminListener {
	
	protected final MCAdmin plugin;
	protected final PlayerHelper playerHelper;
	
	protected BaseListener(){
		plugin = MCAdmin.instance;
		playerHelper = plugin.playerHelper;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}
