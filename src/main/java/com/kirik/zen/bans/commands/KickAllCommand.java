package com.kirik.zen.bans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.core.util.Utils;
import com.kirik.zen.main.ZenCommandException;

@Names("kickall")
@Help("kicks all players on the server")
@Usage("/kickall [reason]")
@Permission("zen.users.kickall")
public class KickAllCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		if(!(commandSender instanceof Player)) {
			final String reason = Utils.concatArray(args, 1, "Kicked");
			
			for(Player ply : plugin.getServer().getOnlinePlayers()){
				KickCommand.kickPlayer(commandSender, ply, reason);
			}
			playerHelper.sendServerMessage("CONSOLE kicked all players");
			return;
		}
		
		final String reason = Utils.concatArray(args, 1, "Kicked");
		
		for(Player ply : plugin.getServer().getOnlinePlayers()){
			KickCommand.kickPlayer(commandSender, ply, reason);
		}
		playerHelper.sendServerMessage(commandSender.getName() + " kicked all players");
	}
}
