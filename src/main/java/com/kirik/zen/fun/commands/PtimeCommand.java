package com.kirik.zen.fun.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("ptime")
@Help("Sets the time for an individual player")
@Usage("/ptime <day|night|sunset|sunrise|current|none>")
@Permission("zen.vip.ptime")
public class PtimeCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		Player player = (Player)commandSender;
		String time = args[0];
		switch(time) {
		case "day":
			player.setPlayerTime(4283, true);
			break;
		case "sunrise":
			player.setPlayerTime(0, true);
			break;
		case "night":
			player.setPlayerTime(13800, true);
			break;
		case "sunset":
			player.setPlayerTime(11616, true);
			break;
		case "current":
		case "none":
			player.resetPlayerTime();
			break;
		}
		playerHelper.sendDirectedMessage(commandSender, "Time set to " + time.toLowerCase());
	}
}
