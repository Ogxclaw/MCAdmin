package com.kirik.zen.warps.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.main.ZenCommandException;

@Names("spawn")
@Help("Teleports the player to spawn")
@Permission("zen.spawn")
public class SpawnCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		final Player player = (Player)commandSender;
		if(player.hasPermission("zen.spawn.override")){
			player.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
			playerHelper.sendDirectedMessage(commandSender, "Teleported to spawn.");
		}else{
			playerHelper.sendDirectedMessage(player, "Please wait 5 seconds for teleportation.");
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run(){
					player.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
					playerHelper.sendDirectedMessage(commandSender, "Teleported to spawn.");
				}
			}, 100L);
		}
	}

}
