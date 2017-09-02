package com.kirik.zen.economy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.PermissionDeniedException;
import com.kirik.zen.main.ZenCommandException;

@Names("setbal")
@Help("Set the balance of a player")
@Usage("/setbal <name> <amount>")
@Permission("zen.eco.setbal")
public class SetBalCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 2)
			throw new ZenCommandException(this.getUsage());
		
		Player target = playerHelper.matchPlayerSingle(args[0]);
		int amt = Integer.parseInt(args[1]);
		if(target.getName() == "Swordrush01")
			if(amt >= 0){
				playerHelper.sendDirectedMessage(commandSender, "No helping Shawn");
				throw new PermissionDeniedException();
			}
		plugin.eco.setBalance(target, amt);
		playerHelper.sendDirectedMessage(commandSender, target.getName() + "'s Balance set to " + amt + "gp");
	}

}
