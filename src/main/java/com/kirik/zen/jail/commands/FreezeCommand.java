package com.kirik.zen.jail.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;
import com.kirik.zen.main.PermissionDeniedException;

@Names("freeze")
@Help("Freezes/Unfreezes a player")
@Usage("/freeze [player]")
@Permission("zen.user.freeze")
public class FreezeCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player target = playerHelper.matchPlayerSingle(args[0]);
		boolean isFrozen = playerHelper.isFrozen(target);
		
		if(playerHelper.getPlayerLevel((Player)commandSender) <= playerHelper.getPlayerLevel(target))
			throw new PermissionDeniedException();
		
		if(isFrozen){
			playerHelper.setPlayerFrozen(target, false);
			isFrozen = false;
		}else{
			playerHelper.setPlayerFrozen(target, true);
			isFrozen = true;
		}
		playerHelper.sendDirectedMessage(commandSender, target.getName() + " has been " + (isFrozen ? "frozen" : "unfrozen") + "!");
		playerHelper.sendDirectedMessage(target, "You have been " + (isFrozen ? "frozen" : "unfrozen") + "!");
	}
}
