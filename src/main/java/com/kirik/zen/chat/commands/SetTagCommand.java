package com.kirik.zen.chat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.core.util.Utils;
import com.kirik.zen.main.PermissionDeniedException;
import com.kirik.zen.main.ZenCommandException;

//TODO make new "tag" command. Lets you pick from a set of tags, should you have the permission to pick it
//TODO used for donators picking their suffixes
//TODO make a setperm <player> <perm> command so a player can receive a permission upon donating.
@Names("settag")
@Help("Changes or resets the tag of a player")
@Usage("/settag <player> <tag|none>")
@Permission("zen.settag")
public class SetTagCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		if(!(commandSender instanceof Player)) {
			Player target = playerHelper.matchPlayerSingle(args[0]);
			
			String tag = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
			if(tag.equals("none")){
				playerHelper.setPlayerSuffix(target, "");
				playerHelper.sendServerMessage("CONSOLE reset the tag of " + target.getName() + "\u00a7f!");
				
			}else{
				playerHelper.setPlayerSuffix(target, " " + tag);
				playerHelper.sendServerMessage("CONSOLE set tag of " + target.getName() + " to " + tag + "\u00a7f!");
			}
			return;
		}
		
		Player player = (Player)commandSender;
		Player target = playerHelper.matchPlayerSingle(args[0]);
		
		if(playerHelper.getPlayerLevel(player) <= playerHelper.getPlayerLevel(target) && target != player)
			throw new PermissionDeniedException();
		
		String tag = Utils.concatArray(args, 1, "").replace('$', '\u00a7');
		if(tag.equals("none")){
			playerHelper.setPlayerSuffix(target, "");
			playerHelper.sendServerMessage(commandSender.getName() + " reset the tag of " + target.getName() + "\u00a7f!");
			
		}else{
			playerHelper.setPlayerSuffix(target, " " + tag);
			playerHelper.sendServerMessage(commandSender.getName() + " set tag of " + target.getName() + " to " + tag + "\u00a7f!");
		}
	}
	
}
