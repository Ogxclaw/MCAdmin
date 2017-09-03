package com.kirik.zen.chat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.core.util.PlayerNotFoundException;
import com.kirik.zen.core.util.Utils;
import com.kirik.zen.main.PermissionDeniedException;
import com.kirik.zen.main.ZenCommandException;

@Names({"setnick", "nick"})
@Help("Sets the nickname of a player")
@Usage("/setnick <name> <newName|none>")
@Permission("zen.setnick")
public class SetNickCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		if(!(commandSender instanceof Player)) {
			String targetName = playerHelper.completePlayerName(args[0], false);
			
			if(targetName == null)
				throw new PlayerNotFoundException();
			
			Player targetPlayer = playerHelper.matchPlayerSingle(args[0]);
			
			String tag = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
			if(tag.equals("none")){
				targetPlayer.setDisplayName(targetPlayer.getName());
				playerHelper.sendServerMessage("CONSOLE reset the nickname of " + targetName + "\u00a7f!");
				
			}else{
				targetPlayer.setDisplayName(tag);
				playerHelper.sendServerMessage("CONSOLE set the nickname of " + targetName + " to " + tag + "\u00a7f!");
			}
			return;
		}
		
		String targetName = playerHelper.completePlayerName(args[0], false);
		
		if(targetName == null)
			throw new PlayerNotFoundException();
		
		Player targetPlayer = playerHelper.matchPlayerSingle(args[0]);
		
		int senderLevel = playerHelper.getPlayerLevel((Player)commandSender);
		int targetLevel = playerHelper.getPlayerLevel(targetPlayer);
		
		if(senderLevel <= targetLevel && targetPlayer != (Player)commandSender)
			throw new PermissionDeniedException();
		
		String tag = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
		if(tag.equals("none")){
			targetPlayer.setDisplayName(targetPlayer.getName());
			playerHelper.sendServerMessage(commandSender.getName() + " reset the nickname of " + targetName + "\u00a7f!");
			
		}else{
			targetPlayer.setDisplayName(tag);
			playerHelper.sendServerMessage(commandSender.getName() + " set the nickname of " + targetName + " to " + tag + "\u00a7f!");
		}
	}

}
