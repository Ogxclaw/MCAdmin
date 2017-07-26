package com.kirik.mcadmin.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.BaseCommand.Name;
import com.kirik.mcadmin.core.util.MCAdminCommandException;
import com.kirik.mcadmin.core.util.Utils;
 
//TODO This kinda works, but also really doesn't.
@Name("setnick")
public class SetNickCommand extends BaseCommand {
	
	@Override
	public boolean onCommandPlayer(Player player, Command command, String s, String[] args) throws MCAdminCommandException {
		String targetName = playerHelper.completePlayerName(args[0], false);
		
		if(targetName == null)
			throw new MCAdminCommandException("No player found.");
		
		Player targetPlayer = playerHelper.matchPlayerSingle(args[0]);
		
		String newNick = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
		if(newNick.equals("none")){
			targetPlayer.setDisplayName(targetName);
			playerHelper.setPlayerNick(targetName, null);
			playerHelper.sendServerMessage(player.getName() + " reset the nickname of " + targetName + "\u00a7f!");
			
		}else{
			targetPlayer.setDisplayName(newNick);
			playerHelper.setPlayerNick(targetName, newNick);
			playerHelper.sendServerMessage(player.getName() + " set nickname of " + targetName + " to " + newNick + "\u00a7f!");
		}
		return true;
	}

}
