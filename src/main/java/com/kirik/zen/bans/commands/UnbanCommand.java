package com.kirik.zen.bans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.core.util.PlayerNotFoundException;
import com.kirik.zen.main.ZenCommandException;

@Names({"unban", "pardon"})
@Help("Unbans a user from the server.")
@Usage("/unban <name>")
@Permission("zen.users.unban")
public class UnbanCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		if(!(commandSender instanceof Player)) {
			String targetName = args[0].toLowerCase();
			String targetUUID = playerHelper.getUUIDfromPlayer(targetName);
			if(targetUUID == null)
				throw new PlayerNotFoundException();
			if(!playerHelper.isBanned(targetUUID))
				throw new ZenCommandException(targetName + " is not banned!");
			playerHelper.unbanPlayer(targetUUID);
			playerHelper.sendServerMessage("CONSOLE unbanned " + targetName);
			return;
		}
		
		String targetName = args[0].toLowerCase();
		String targetUUID = playerHelper.getUUIDfromPlayer(targetName);
		if(targetUUID == null)
			throw new PlayerNotFoundException();
		if(!playerHelper.isBanned(targetUUID))
			throw new ZenCommandException(targetName + " is not banned!");
		playerHelper.unbanPlayer(targetUUID);
		playerHelper.sendServerMessage(commandSender.getName() + " unbanned " + targetName);
	}

}
