package com.kirik.zen.chat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.kirik.zen.fun.Suffixes;
import com.kirik.zen.main.listeners.BaseListener;

public class SuffixListener extends BaseListener {
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		if(e.getInventory().getTitle().equals("Available Suffixes")) {
			try {
				for(Suffixes suff : Suffixes.values()) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(suff.getDisplayName())) {
						if(e.getWhoClicked().hasPermission(suff.getPerm())) {
							playerHelper.setPlayerSuffix((Player)e.getWhoClicked(), " " + suff.getDisplayName());
							playerHelper.sendDirectedMessage((Player)e.getWhoClicked(), "Suffix set to " + suff.getDisplayName());
							e.setCancelled(true);
							e.getWhoClicked().closeInventory();
						}else {
							playerHelper.sendDirectedMessage((Player)e.getWhoClicked(), "Permission Denied!", '4');
						}
					}
				}
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("\u00a7cReset Tag")) {
					playerHelper.setPlayerSuffix((Player)e.getWhoClicked(), "");
					playerHelper.sendDirectedMessage((Player)e.getWhoClicked(), "Suffix reset!");
					e.setCancelled(true);
					e.getWhoClicked().closeInventory();
				}
			}catch(NullPointerException ex) {
				e.setCancelled(true);
			}
		}
	}
}
