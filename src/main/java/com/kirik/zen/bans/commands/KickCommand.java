package com.kirik.zen.bans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.core.util.Utils;
import com.kirik.zen.main.ZenCommandException;
import com.kirik.zen.main.PermissionDeniedException;

@Names("kick")
@Help("Kicks specified user")
@Usage("/kick <name> [reason]")
@Permission("zen.users.kick")
public class KickCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		final Player target = playerHelper.matchPlayerSingle(args[0]);
		
		if(playerHelper.getPlayerLevel((Player)commandSender) < playerHelper.getPlayerLevel(target))
			throw new PermissionDeniedException();
		
		final String reason = Utils.concatArray(args, 1, "Kicked");
		
		kickPlayer(commandSender, target, reason);
		playerHelper.sendServerMessage(commandSender.getName() + " kicked " + target.getName() + " ((" + reason + "))");
	}
	
	public static void kickPlayer(CommandSender sender, Player target, String reason){
		target.kickPlayer("[Kicked] " + sender.getName() + ": " + reason);
	}

}
