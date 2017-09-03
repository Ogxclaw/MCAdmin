package com.kirik.zen.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("fly")
@Help("Enables you to fly")
@Usage("/fly")
@Permission("zen.admin.fly")
public class FlyCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		if(player.getAllowFlight()){
			player.setAllowFlight(false);
			playerHelper.sendDirectedMessage(commandSender, "Flight disabled.");
		}else{
			player.setAllowFlight(true);
			playerHelper.sendDirectedMessage(commandSender, "Flight enabled.");
		}
	}

}
