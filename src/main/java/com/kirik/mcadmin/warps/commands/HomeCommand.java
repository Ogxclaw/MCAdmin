package com.kirik.mcadmin.warps.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.main.MCAdminCommandException;

@Names("home")
@Help("Teleports player to their home")
@Usage("")
@Permission("mcadmin.home")
public class HomeCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		Player player = (Player)commandSender;
		player.teleport(playerHelper.getHome(player));
		playerHelper.sendDirectedMessage(commandSender, "Teleported home.");
	}
	
}