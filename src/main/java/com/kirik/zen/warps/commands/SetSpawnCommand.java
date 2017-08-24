package com.kirik.zen.warps.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.main.ZenCommandException;

@Names("setspawn")
@Help("Sets the default spawn location of all players")
@Permission("zen.admin.setspawn")
public class SetSpawnCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		//TODO kinda works but really doesn't
		//TODO Make the spawn point go to a direct set of coords from a config file if set, if not set then go to default getSpawnLocation
		//TODO dont make /spawn until this is fixed and works correctly.
		Player player = (Player)commandSender;
		
		Location playerLocation = player.getLocation();
		plugin.getServer().getWorld("world").setSpawnLocation((int)playerLocation.getX(), (int)playerLocation.getY(), (int)playerLocation.getZ());
		playerHelper.sendDirectedMessage(player, "Spawn point set.");
	}

}
