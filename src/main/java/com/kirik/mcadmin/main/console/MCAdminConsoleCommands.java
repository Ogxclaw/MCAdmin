package com.kirik.mcadmin.main.console;

import com.kirik.mcadmin.core.MCAdmin;

public class MCAdminConsoleCommands {

	public MCAdminConsoleCommands(MCAdmin plugin){
		plugin.getCommand("mca").setExecutor(new MCAdminCommandExecutor(plugin));
	}
}
