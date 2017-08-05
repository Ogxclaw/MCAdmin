package com.kirik.mcadmin.bans.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import com.kirik.mcadmin.config.BansConfiguration;
import com.kirik.mcadmin.main.listeners.BaseListener;

public class BansPlayerListener extends BaseListener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e){
		String uuid = e.getUniqueId().toString();
		
		BansConfiguration bans = new BansConfiguration();
		if(bans.getBansConfig().getBoolean(uuid + ".isBanned") == true){
			e.disallow(Result.KICK_BANNED, "[MCADMIN] Banned: " + bans.getBansConfig().get(uuid + ".reason"));
		}
	}

}
