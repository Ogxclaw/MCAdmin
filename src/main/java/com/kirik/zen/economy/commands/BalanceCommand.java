package com.kirik.zen.economy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names({"bal", "balance"})
@Help("Checks player balance")
@Usage("/bal [name]")
@Permission("zen.eco.bal")
public class BalanceCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		if(args.length < 1){
			int bal = plugin.eco.getBalance(player);
			playerHelper.sendDirectedMessage(commandSender, "Your Balance: " + bal + "gp");
		}else{
			if(player.hasPermission("zen.eco.bal.others")){
				Player target = playerHelper.matchPlayerSingle(args[0]);
				int bal = plugin.eco.getBalance(target);
				playerHelper.sendDirectedMessage(commandSender, target.getName() + "'s Balance: " + bal + "gp"); 
			}
		}
	}

}
