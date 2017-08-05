package com.kirik.zen.main.console;

import com.kirik.zen.core.Zen;

public class ZenConsoleCommands {

	public ZenConsoleCommands(Zen plugin){
		plugin.getCommand("zen").setExecutor(new ZenCommandExecutor(plugin));
	}
}
