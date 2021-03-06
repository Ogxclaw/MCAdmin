package com.kirik.zen.teleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;
import com.kirik.zen.teleport.Request;

@Names({"tpdeny", "tpdecline"})
@Usage("/tpdecline")
@Help("Declines the current tp request")
@Permission("zen.teleport.request")
public class TpDenyCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		Player byPlayer = null;
		if(args.length > 0) {
			byPlayer = playerHelper.matchPlayerSingle(args[0]);
		}
		Request req = Request.getRequest((Player)commandSender, byPlayer);
		if(req == null) {
			throw new ZenCommandException("Sorry, no pending request found!");
		} else {
			playerHelper.sendDirectedMessage(commandSender, "Request declined!");
			req.decline();
		}
	}

}
