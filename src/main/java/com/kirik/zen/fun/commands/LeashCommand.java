package com.kirik.zen.fun.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.PermissionDeniedException;
import com.kirik.zen.main.ZenCommandException;

@Names("leash")
@Help("Leashes or unleashes a player.")
@Usage("<name>")
@Permission("zen.players.leash")
public class LeashCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException("Not enough arguments");
		
		Player targetPlayer = playerHelper.matchPlayerSingle(args[0]);
		
		if(playerHelper.getPlayerLevel((Player)commandSender) <= playerHelper.getPlayerLevel(targetPlayer))
			throw new PermissionDeniedException();
		
		if(playerHelper.toggleLeash((Player)commandSender, targetPlayer))
			playerHelper.sendServerMessage(commandSender.getName() + " leashed " + targetPlayer.getName());
		else
			playerHelper.sendServerMessage(commandSender.getName() + " unleashed " + targetPlayer.getName());
	}

}
