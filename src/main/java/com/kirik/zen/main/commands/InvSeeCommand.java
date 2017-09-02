package com.kirik.zen.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("invsee")
@Help("Opens the inventory of another player")
@Usage("/invsee <name>")
@Permission("zen.admin.invsee")
public class InvSeeCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		Player target = playerHelper.matchPlayerSingle(args[0]);
		Inventory inv = target.getInventory();
		Player player = (Player)commandSender;
		player.openInventory(inv);
		playerHelper.sendDirectedMessage(commandSender, "Opened " + target.getName() + "'s inventory");
	}
	

}
