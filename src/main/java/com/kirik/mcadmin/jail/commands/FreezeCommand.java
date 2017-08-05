package com.kirik.mcadmin.jail.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.main.MCAdminCommandException;
import com.kirik.mcadmin.main.PermissionDeniedException;

@Names("freeze")
@Help("Freezes/Unfreezes a player")
@Usage("/freeze [player]")
@Permission("mcadmin.user.freeze")
public class FreezeCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
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
