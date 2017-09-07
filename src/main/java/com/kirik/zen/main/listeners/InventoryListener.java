package com.kirik.zen.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener extends BaseListener {
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		if(e.getInventory().getTitle().equals("\u00a75[Zen] \u00a78Help Menu (Page 1)")) {
			try {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("\u00a7aNext Page")) {
					Inventory invPage2 = e.getWhoClicked().getServer().createInventory(e.getWhoClicked(), 54, "\u00a75[Zen] \u00a78Help Menu (Page 2)");
					e.getWhoClicked().openInventory(invPage2);			
				}
				e.setCancelled(true);
			}catch(NullPointerException ex) {
				e.setCancelled(true);
			}
		}
	}
	
	/*@EventHandler
	public void onInvOpen(InventoryOpenEvent e) {
		if(e.getInventory().getName().equals("\u00a75[Zen] \u00a78Help Menu (Page 2)")) {
			e.getInventory().clear();
			for(int i = 0; i < HelpCommand.page2.size(); i++) {
				e.getInventory().setItem(i, HelpCommand.page2.get(i));
			}
		}
	}*/

}
