package com.kirik.mcadmin.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.core.util.PlayerFindException;

@Names({ "who", "list" })
@Help("Prints user list if used without params or information about a specified user")
@Usage("[player]")
@Permission("mcadmin.who")
public class ListCommand extends ICommand {

	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws PlayerFindException {
		plugin.playerHelper.sendDirectedMessage((Player) commandSender, "Hello!!");
	}

}
