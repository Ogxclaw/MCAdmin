package com.kirik.zen.warps.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("setspawn")
@Help("Sets the default spawn location of all players")
@Usage("/setspawn")
@Permission("zen.admin.setspawn")
public class SetSpawnCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		//TODO does not account for pitch/yaw
		Player player = (Player)commandSender;
		
		Location playerLocation = player.getLocation();
		plugin.getServer().getWorld("world").setSpawnLocation((int)playerLocation.getX(), (int)playerLocation.getY(), (int)playerLocation.getZ());
		playerHelper.sendDirectedMessage(player, "Spawn point set.");
	}

}
