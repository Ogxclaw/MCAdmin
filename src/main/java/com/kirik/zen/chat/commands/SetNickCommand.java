package com.kirik.zen.chat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.core.util.Utils;
import com.kirik.zen.core.util.ZenPlayer;
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
			Player target = playerHelper.matchPlayerSingle(args[0]);
			
			String tag = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
			if(tag.equals("none")){
				target.setDisplayName(target.getName());
				playerHelper.sendServerMessage("CONSOLE reset the nickname of " + target.getName() + "\u00a7f!");
				
			}else{
				target.setDisplayName(tag);
				playerHelper.sendServerMessage("CONSOLE set the nickname of " + target.getName() + " to " + tag + "\u00a7f!");
			}
			return;
		}
		
		Player player = (Player)commandSender;
		Player target = playerHelper.matchPlayerSingle(args[0]);
		
		if(playerHelper.getPlayerLevel(player) <= playerHelper.getPlayerLevel(target) && target != player)
			throw new PermissionDeniedException();
		
		String tag = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
		if(tag.equals("none")){
			target.setDisplayName(target.getName());
			playerHelper.sendServerMessage(commandSender.getName() + " reset the nickname of " + target.getName() + "\u00a7f!");
			
		}else{
			target.setDisplayName(tag);
			playerHelper.sendServerMessage(commandSender.getName() + " set the nickname of " + target.getName() + " to " + tag + "\u00a7f!");
		}
		
		//TODO getting there with the player rework thing...
		/*Player player = (Player)commandSender;
		ZenPlayer zenPlayer = new ZenPlayer(player.getUniqueId());
		Player target = playerHelper.matchPlayerSingle(args[0]);
		
		if(playerHelper.getPlayerLevel(player) <= playerHelper.getPlayerLevel(target) && target != player)
			throw new PermissionDeniedException();
		
		String tag = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
		if(tag.equals("none")){
			zenPlayer.setDisplayName(zenPlayer.getName());
			playerHelper.sendServerMessage(commandSender.getName() + " reset the nickname of " + target.getName() + "\u00a7f!");
			
		}else{
			zenPlayer.setDisplayName(tag);
			playerHelper.sendServerMessage(commandSender.getName() + " set the nickname of " + target.getName() + " to " + tag + "\u00a7f!");
		}*/
	}

}
