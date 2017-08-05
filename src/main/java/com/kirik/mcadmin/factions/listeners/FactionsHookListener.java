package com.kirik.mcadmin.factions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.kirik.mcadmin.main.listeners.BaseListener;
import com.massivecraft.factions.event.EventFactionsCreate;

public class FactionsHookListener extends BaseListener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void createFaction(EventFactionsCreate event) {
		plugin.playerHelper.sendServerMessage("Faction " + event.getFactionName() + " created!");
	}
	

}
