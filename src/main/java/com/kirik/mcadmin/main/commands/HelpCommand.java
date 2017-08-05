package com.kirik.mcadmin.main.commands;

import java.util.Map;
import java.util.PriorityQueue;

import org.bukkit.command.CommandSender;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.main.MCAdminCommandException;

@Names("mcahelp")
@Help("Prints a list of available commands or information about the specified command.")
@Usage("[<command>]")
@Permission("mcadmin.help")
public class HelpCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		Map<String, ICommand> commands = plugin.commandSystem.getCommands();
		
		if(args.length > 0){
			ICommand val = commands.get(args[0]);
			if(val == null || !val.canPlayerUseCommand(commandSender)){
				throw new MCAdminCommandException("Command not found!");
			}
			for(String line : val.getHelp().split("\n")){
				playerHelper.sendDirectedMessage(commandSender, line);
			}
			playerHelper.sendDirectedMessage(commandSender, "Usage: /" + args[0] + " " + val.getUsage());
		}else{
			String ret = "Available commands: /";
			for(String key : new PriorityQueue<>(commands.keySet())){
				if(key == "\u00a7")
					continue;
				
				ICommand val = commands.get(key);
				if(!val.canPlayerUseCommand(commandSender))
					continue;
				
				ret += key + ", /";
			}
			ret = ret.substring(0, ret.length() - 3);
			playerHelper.sendDirectedMessage(commandSender, ret);
		}
	}
}
