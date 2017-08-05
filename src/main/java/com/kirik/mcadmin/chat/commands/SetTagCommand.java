package com.kirik.mcadmin.chat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.core.util.PlayerNotFoundException;
import com.kirik.mcadmin.core.util.Utils;
import com.kirik.mcadmin.main.MCAdminCommandException;

@Names("settag")
@Help("Changes or resets the tag of a player")
@Usage("[player|none] <tag>")
@Permission("mcadmin.settag")
public class SetTagCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		String targetName = playerHelper.completePlayerName(args[0], false);
		
		if(targetName == null)
			throw new PlayerNotFoundException();
		
		Player targetPlayer = playerHelper.matchPlayerSingle(args[0]);
		
		String tag = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
		if(tag.equals("none")){
			playerHelper.setPlayerSuffix(targetPlayer, "");
			playerHelper.sendServerMessage(commandSender.getName() + " reset the tag of " + targetName + "\u00a7f!");
			
		}else{
			playerHelper.setPlayerSuffix(targetPlayer, " " + tag);
			playerHelper.sendServerMessage(commandSender.getName() + " set tag of " + targetName + " to " + tag + "\u00a7f!");
		}
	}
	
}
