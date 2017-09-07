package com.kirik.zen.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("feed")
@Help("Feeds the player to maximum health")
@Usage("/feed")
@Permission("zen.vip.feed")
public class FeedCommand extends ICommand {

	//TODO timer
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		player.setFoodLevel(20);
		player.setSaturation(20);
		playerHelper.sendDirectedMessage(commandSender, "Consider yourself fed!");
	}
}
