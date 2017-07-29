package com.kirik.mcadmin.permissions.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.core.util.PlayerNotFoundException;
import com.kirik.mcadmin.main.MCAdminCommandException;

@Names("setrank")
@Help("Changes or resets the tag of a player")
@Usage("[player] <rank>")
@Permission("mcadmin.setrank")
public class SetRankCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		String targetName = playerHelper.completePlayerName(args[0], false);
		
		if(targetName == null)
			throw new PlayerNotFoundException();
		
		Player targetPlayer = playerHelper.matchPlayerSingle(args[0]);
		
		String rank = args[1];
		String[] groups = plugin.permission.getGroups();
		//playerHelper.setPlayerPrefix(targetPlayer, newNick + " ");
		if(Arrays.asList(groups).contains(rank)){
			plugin.permission.playerRemoveGroup(targetPlayer, plugin.playerHelper.getPlayerRank(targetPlayer));
			plugin.permission.playerAddGroup(targetPlayer, rank);
		}else{
			throw new MCAdminCommandException("Rank " + rank + " not found!");
		}
		
		playerHelper.sendServerMessage(commandSender.getName() + " set rank of " + targetName + " to " + rank + "\u00a7f!");
	}
	
}