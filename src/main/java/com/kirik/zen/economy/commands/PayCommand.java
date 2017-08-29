package com.kirik.zen.economy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.economy.NotEnoughMoneyException;
import com.kirik.zen.main.ZenCommandException;

@Names("pay")
@Help("Pay a player a selected amount")
@Usage("/pay <name> <amount>")
@Permission("zen.eco.pay")
public class PayCommand extends ICommand {
	
	@Override
	public void run(CommandSender sender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player target = playerHelper.matchPlayerSingle(args[0]);
		int amt = Integer.parseInt(args[1]);
		
		try {
			plugin.eco.payPlayer((Player)sender, target, amt);
		}catch(NotEnoughMoneyException e){
			playerHelper.sendDirectedMessage(sender, e.getMessage());
			return;
		}
		playerHelper.sendDirectedMessage(sender, "Paid " + target.getName() + " " + amt + "gp");
		playerHelper.sendDirectedMessage(target, "Received " + amt + "gp from " + target.getName());
	}

}
