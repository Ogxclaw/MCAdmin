package com.kirik.zen.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener extends BaseListener {
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		if(e.getInventory().getTitle().equals("\u00a75[Zen] \u00a78Help Menu"))
			e.setCancelled(true);
		
	}

}
