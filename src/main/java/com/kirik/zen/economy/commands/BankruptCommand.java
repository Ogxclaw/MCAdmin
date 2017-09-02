package com.kirik.zen.economy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("bankrupt")
@Help("Forces a player to sell their liquidized assets")
@Usage("/bankrupt [name]")
@Permission("zen.eco.bankrupt")
public class BankruptCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		Player target = playerHelper.matchPlayerSingle(args[0]);
		plugin.eco.setBalance(target, Integer.MIN_VALUE);
		playerHelper.sendDirectedMessage(commandSender, "Bankrupted " + target.getName());
		playerHelper.sendDirectedMessage(target, "Congratulations! The economy crashed...");
		playerHelper.sendDirectedMessage(target, "You kinda got fucked...");
	}

}
