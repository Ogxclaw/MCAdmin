package com.kirik.zen.bans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.bans.MCBansResolver;
import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("lookup")
@Help("Gets ban and alt information about specified user (MCBANS AND MCADMIN INFO ONLY)")
@Usage("<name>")
@Permission("zen.users.lookup")
public class LookupCommand extends ICommand {

	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player target = playerHelper.matchPlayerSingle(args[0]);
		playerHelper.sendDirectedMessage(commandSender, "Looking up information on " + target.getName() + "...");
		MCBansResolver.checkPlayer(target.getUniqueId().toString());	
	}
}
