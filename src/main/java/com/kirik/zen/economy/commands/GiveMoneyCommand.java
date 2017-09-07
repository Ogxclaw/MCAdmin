package com.kirik.zen.economy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.economy.Economy;
import com.kirik.zen.main.ZenCommandException;

@Names({"givemoney", "gibemealottamonee"})
@Permission("zen.eco.givemoney")
@Usage("/givemoney <name> <amount>")
@Help("Gives the player money, from admin bank (infinite)")
public class GiveMoneyCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 2)
			throw new ZenCommandException(this.getUsage());
		
		if(!(commandSender instanceof Player)) {
			Player target = playerHelper.matchPlayerSingle(args[0]);
			int amt = Integer.parseInt(args[1]);
			Economy eco = new Economy(plugin);
			eco.addToBalance(target, amt);
			playerHelper.sendDirectedMessage(target, "CONSOLE payed you " + amt + "gp");
		}else {
			playerHelper.sendDirectedMessage(commandSender, "Can only be used by CONSOLE");
		}
	}

}
