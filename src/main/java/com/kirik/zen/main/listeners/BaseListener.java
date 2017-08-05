package com.kirik.zen.main.listeners;

import org.bukkit.event.Listener;

import com.kirik.zen.componentsystem.ZenListener;
import com.kirik.zen.core.Zen;
import com.kirik.zen.core.util.PlayerHelper;

public abstract class BaseListener implements Listener, ZenListener {
	
	protected final Zen plugin;
	protected final PlayerHelper playerHelper;
	
	protected BaseListener(){
		plugin = Zen.instance;
		playerHelper = plugin.playerHelper;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}
