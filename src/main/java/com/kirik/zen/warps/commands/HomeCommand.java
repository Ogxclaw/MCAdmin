package com.kirik.zen.warps.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("home")
@Help("Teleports player to their home")
@Usage("")
@Permission("zen.home")
public class HomeCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		player.teleport(playerHelper.getHome(player));
		playerHelper.sendDirectedMessage(commandSender, "Teleported home.");
	}
	
}