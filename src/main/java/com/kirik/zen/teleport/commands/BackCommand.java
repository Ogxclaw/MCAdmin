package com.kirik.zen.teleport.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.main.ZenCommandException;

@Names("back")
@Help("Teleports a player to their previous location")
@Usage("/back")
@Permission("zen.vip.back")
public class BackCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		Location loc = (Location) playerConfig.getPlayerConfig().get("previousLocation");
		if(loc == null){
			throw new ZenCommandException("You have not previously teleported!");
		}
		player.teleport(loc);
		playerHelper.sendDirectedMessage(commandSender, "Teleported to your previous location");
	}
}
