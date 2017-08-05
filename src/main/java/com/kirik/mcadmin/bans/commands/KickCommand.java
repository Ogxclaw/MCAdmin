package com.kirik.mcadmin.bans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.core.util.Utils;
import com.kirik.mcadmin.main.MCAdminCommandException;
import com.kirik.mcadmin.main.PermissionDeniedException;

@Names("kick")
@Help("Kicks specified player")
@Usage("<name> [reason]")
@Permission("mcadmin.users.kick")
public class KickCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
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
