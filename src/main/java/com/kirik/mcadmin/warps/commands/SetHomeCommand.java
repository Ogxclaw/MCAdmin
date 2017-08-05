package com.kirik.mcadmin.warps.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.main.MCAdminCommandException;

@Names("sethome")
@Help("Sets a player's home to their current position")
@Usage("")
@Permission("mcadmin.sethome")
public class SetHomeCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		Player player = (Player)commandSender;
		playerHelper.setHome(player);
		playerHelper.sendDirectedMessage(commandSender, "Home set at current position.");
	}
	
}