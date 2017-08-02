package com.kirik.mcadmin.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.main.MCAdminCommandException;

@Names("sethome")
@Help("Sets a player's home to their current position")
@Usage("")
@Permission("mcadmin.sethome")
public class SetHomeCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		/*double x = plugin.getConfig().getDouble("players." + commandSender.getName() + ".home.x");
		double y = plugin.getConfig().getDouble("players." + commandSender.getName() + ".home.y");
		double z = plugin.getConfig().getDouble("players." + commandSender.getName() + ".home.z");
		Player player = (Player)commandSender;
		player.teleport(new Location(player.getWorld(), x, y, z));*/
		Player player = (Player)commandSender;
		plugin.config.getHomeConfig().set("players." + player.getName() + ".home.x", player.getLocation().getX());
		plugin.config.getHomeConfig().set("players." + player.getName() + ".home.y", player.getLocation().getY());
		plugin.config.getHomeConfig().set("players." + player.getName() + ".home.z", player.getLocation().getZ());
		plugin.config.getHomeConfig().set("players." + player.getName() + ".home.pitch", player.getLocation().getPitch());
		plugin.config.getHomeConfig().set("players." + player.getName() + ".home.yaw", player.getLocation().getYaw());
		plugin.config.saveHomeConfig(); // doesnt work
		plugin.playerHelper.sendDirectedMessage(player, "Home set.");
	}
	
}