package com.kirik.zen.chat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("mute")
@Usage("<name>")
@Permission("zen.chat.mute")
public class MuteCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player target = playerHelper.matchPlayerSingle(args[0]);
		boolean isMuted = playerHelper.isMuted(target);
		if(isMuted){
			playerHelper.setPlayerMuted(target, false);
		}else{
			playerHelper.setPlayerMuted(target, true);
		}
		playerHelper.sendServerMessage(commandSender.getName() + " has " + (isMuted ? "unmuted " : "muted ") + target.getName() + "!");
	}

}
