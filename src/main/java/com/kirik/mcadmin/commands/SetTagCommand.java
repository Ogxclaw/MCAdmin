package com.kirik.mcadmin.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.BaseCommand.Name;
import com.kirik.mcadmin.core.util.MCAdminCommandException;
import com.kirik.mcadmin.core.util.Utils;
 
//TODO This kinda works, but also really doesn't.
@Name("settag")
public class SetTagCommand extends BaseCommand {
	
	@Override
	public boolean onCommandPlayer(Player player, Command command, String s, String[] args) throws MCAdminCommandException {
		String targetName = playerHelper.completePlayerName(args[0], false);
		
		if(targetName == null)
			throw new MCAdminCommandException("No player found.");
		
		Player targetPlayer = playerHelper.matchPlayerSingle(args[0]);
		
		String newNick = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
		if(newNick.equals("none")){
			//targetPlayer.setDisplayName(targetName);
			playerHelper.setPlayerPrefix(targetPlayer, null);
			playerHelper.sendServerMessage(player.getName() + " reset the tag of " + targetName + "\u00a7f!");
			
		}else{
			//targetPlayer.setDisplayName(newNick);
			playerHelper.setPlayerPrefix(targetPlayer, newNick);
			playerHelper.sendServerMessage(player.getName() + " set tag of " + targetName + " to " + newNick + "\u00a7f!");
		}
		return true;
	}

}
