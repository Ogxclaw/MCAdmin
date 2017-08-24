package com.kirik.zen.teleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.main.ZenCommandException;
import com.kirik.zen.teleport.Request;
import com.kirik.zen.teleport.RequestRunnable;

@Names({"summona", "tpahere"})
@Permission("zen.teleport.request")
public class SummonaCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		Player other = playerHelper.matchPlayerSingle(args[0]);
		requestTeleport(player, other, other, player, player.getName() + " wants to summon you!");
		playerHelper.sendDirectedMessage(player, "Requested summoning " + other.getDisplayName());
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
						toTeleport.teleport(target.getLocation());
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
