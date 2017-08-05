package com.kirik.mcadmin.main.console;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.core.util.Utils;

public class MCAdminCommandExecutor implements CommandExecutor {
	
	private MCAdmin plugin;
	
	public MCAdminCommandExecutor(MCAdmin plugin){
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
