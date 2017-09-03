package com.kirik.zen.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("echest")
@Help("Opens your ender chest")
@Usage("/echest [name]")
@Permission("zen.echest")
public class EChestCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		if(args.length == 0) {
			player.openInventory(player.getEnderChest());
		}else if((args.length >= 1) && player.hasPermission("zen.echest.others")) {
			Player target = playerHelper.matchPlayerSingle(args[0]);
			player.openInventory(target.getEnderChest());
			playerHelper.sendDirectedMessage(commandSender, "Opened " + target.getName() + "'s ender chest");
		}
	}

}
