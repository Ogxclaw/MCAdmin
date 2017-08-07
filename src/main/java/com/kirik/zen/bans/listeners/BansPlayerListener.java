package com.kirik.zen.bans.listeners;

import java.time.LocalDate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import com.kirik.zen.main.listeners.BaseListener;

public class BansPlayerListener extends BaseListener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e){
		String uuid = e.getUniqueId().toString();
		String playerName = plugin.getBansConfig().getString(uuid + ".name");
		
		if(plugin.playerHelper.isBanned(uuid)){
			LocalDate date = LocalDate.now();
			int currentDate = date.getDayOfYear();
			int unbanDate = plugin.getBansConfig().getInt(uuid + ".unbanDate");
			String banType = plugin.getBansConfig().getString(uuid + ".banType");
			if(banType.equals("temp")){
				if(unbanDate == currentDate){
					plugin.playerHelper.unbanPlayer(uuid);
					plugin.logToConsole("Temp Banned player \"" + playerName + "\" has been unbanned.");
					plugin.saveBansConfig();
					return;
				}else{
					int finalDate = unbanDate - currentDate;
					if(finalDate < 0) {
						finalDate += 365;
					}
					e.disallow(Result.KICK_BANNED, "\n\u00a74[Zen]\u00a7f Banned: " + plugin.getBansConfig().get(uuid + ".reason") + 
							"\nYou will be unbanned in: \u00a7c" + finalDate + 
							"\u00a7f" + ((finalDate == 1) ? " day" : " days")
							+ "\nAppeal at SITE.com");
				}
			}else
				e.disallow(Result.KICK_BANNED, "\n\u00a74[Zen]\u00a7f Banned: " + plugin.getBansConfig().get(uuid + ".reason") + 
						"\nYou will be unbanned in: \u00a7cNEVER"
						+ "\n\u00a7fAppeal at SITE.com");
		}
	}

}
