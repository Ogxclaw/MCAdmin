package com.kirik.mcadmin.bans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.bans.MCBansResolver;
import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.main.MCAdminCommandException;

@Names("lookup")
@Help("Gets ban and alt information about specified user (MCBANS AND MCADMIN INFO ONLY)")
@Usage("<name>")
@Permission("mcadmin.users.lookup")
public class LookupCommand extends ICommand {

	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		Player target = playerHelper.matchPlayerSingle(args[0]);
		playerHelper.sendDirectedMessage(commandSender, "Looking up information on " + target.getName() + "...");
		MCBansResolver.checkPlayer(target.getUniqueId().toString());	
	}
}
