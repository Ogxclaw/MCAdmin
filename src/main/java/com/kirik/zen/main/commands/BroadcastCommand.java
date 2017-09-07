package com.kirik.zen.main.commands;

import org.bukkit.command.CommandSender;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.core.util.Utils;
import com.kirik.zen.main.ZenCommandException;

@Names("broadcast")
@Usage("/broadcast <text>")
@Help("Broadcasts a message to the server")
@Permission("zen.admin.broadcast")
public class BroadcastCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		String msg = Utils.concatArray(args, 0, "").replace('$', '\u00a7');
		playerHelper.sendServerMessage(msg);
	}
}
