package com.kirik.zen.teleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;
import com.kirik.zen.teleport.Request;
import com.kirik.zen.teleport.RequestRunnable;

@Names("tpa")
@Help("Requests to teleport to a player")
@Usage("/tpa <name>")
@Permission("zen.teleport.request")
public class TpaCommand extends ICommand { 
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		final Player player = (Player)commandSender;
		final Player target = playerHelper.matchPlayerSingle(args[0]);
		//requestTeleport((Player)commandSender, target, (Player)commandSender, target, commandSender.getName() + " would like to teleport to you!");
		requestTeleport(player, target, player, target, player.getName() + " wants to teleport to you!");
		playerHelper.sendDirectedMessage(commandSender, "Requested teleporting to " + target.getDisplayName());
	}
	
	protected void requestTeleport(final Player byPlayer, final Player forPlayer, final Player toTeleport, final Player target, final String msg) {
		new Request(forPlayer, byPlayer, new RequestRunnable() {
			@Override
			public void accept() {
				//toTeleport.teleport(target);
				playerHelper.sendDirectedMessage(byPlayer, "Please wait 5 seconds for teleportation.");
				playerHelper.sendDirectedMessage(forPlayer, "Please wait 5 seconds for teleportation.");
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run(){
						toTeleport.teleport(target);
					}
				}, 100L);
				playerHelper.sendDirectedMessage(byPlayer, "Your teleportation request was accepted!");
			}

			@Override
			public void decline() {
				playerHelper.sendDirectedMessage(byPlayer, "Your teleportation request was declined!");
			}
		}).add(msg);
	}
}
