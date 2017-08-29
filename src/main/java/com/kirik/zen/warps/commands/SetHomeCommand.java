package com.kirik.zen.warps.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("sethome")
@Help("Sets a player's home to \ntheir current position")
@Usage("/sethome")
@Permission("zen.sethome")
public class SetHomeCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		playerHelper.setHome(player);
		playerHelper.sendDirectedMessage(commandSender, "Home set at current position.");
	}
	
}