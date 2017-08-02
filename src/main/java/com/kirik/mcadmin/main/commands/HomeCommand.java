package com.kirik.mcadmin.main.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.main.MCAdminCommandException;

@Names("home")
@Help("Teleports player to their home")
@Usage("")
@Permission("mcadmin.home")
public class HomeCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		double x = plugin.config.getHomeConfig().getDouble("players." + commandSender.getName() + ".home.x");
		double y = plugin.config.getHomeConfig().getDouble("players." + commandSender.getName() + ".home.y");
		double z = plugin.config.getHomeConfig().getDouble("players." + commandSender.getName() + ".home.z");
		float pitch = plugin.config.getHomeConfig().getInt("players." + commandSender.getName() + ".home.pitch");
		float yaw = plugin.config.getHomeConfig().getInt("players." + commandSender.getName() + ".home.yaw");
		Player player = (Player)commandSender;
		Location homeLocation = new Location(player.getWorld(), x, y, z);
		homeLocation.setPitch(pitch);
		homeLocation.setYaw(yaw);
		player.teleport(homeLocation);
		plugin.playerHelper.sendDirectedMessage(player, x + " " + y + " " + z);
		plugin.playerHelper.sendDirectedMessage(player, "Teleported home.");
	}
	
}