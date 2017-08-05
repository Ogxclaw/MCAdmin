package com.kirik.zen.main.console;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.core.Zen;
import com.kirik.zen.core.util.Utils;

public class ZenCommandExecutor implements CommandExecutor {
	
	private Zen plugin;
	
	public ZenCommandExecutor(Zen plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(sender instanceof Player)
			return true;
		plugin.commandSystem.runCommand(sender, Utils.concatArray(args, 0, ""));
		return true;
	}

}
