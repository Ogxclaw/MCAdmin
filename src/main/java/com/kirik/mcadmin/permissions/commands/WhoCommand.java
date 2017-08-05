package com.kirik.mcadmin.permissions.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.core.util.PlayerFindException;
import com.kirik.mcadmin.core.util.Utils;

@Names({ "who", "list" })
@Help("Prints user list if used without params or information about a specified user")
@Usage("[name]")
@Permission("mcadmin.who")
public class WhoCommand extends ICommand {

	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws PlayerFindException {
		if(args.length == 0)
			return;
		
		final Location defaultLocation = new Location(plugin.getOrCreateWorld("world", Environment.NORMAL), Double.POSITIVE_INFINITY, 0, 0);
		final Location location = getCommandSenderLocation(commandSender, false, defaultLocation);
		final World world = location.getWorld();
		final Player target = playerHelper.matchPlayerSingle(args[0], false);
		
		playerHelper.sendDirectedMessage(commandSender, "Name: " + target.getName());
		playerHelper.sendDirectedMessage(commandSender, "Rank: " + playerHelper.getPlayerRank(target));
		playerHelper.sendDirectedMessage(commandSender, "NameTag: " + playerHelper.getPersonalPlayerSuffix(target));
		playerHelper.sendDirectedMessage(commandSender, "World: " + target.getWorld().getName());
		
		final int playerLevel = playerHelper.getPlayerLevel((Player)commandSender);
		
		final List<String> distances = new ArrayList<>();
		final Location targetLocation = target.getLocation();
		
		if(commandSender.hasPermission("mcadmin.who.position") && playerLevel >= playerHelper.getPlayerLevel(target)){
			playerHelper.sendDirectedMessage(commandSender, "Position: " + (int)targetLocation.getX() + ", " + (int)targetLocation.getY() + ", " + (int)targetLocation.getZ());
			
			final Vector offsetFromSpawn = targetLocation.toVector().subtract(world.getSpawnLocation().toVector());
			final long unitsFromSpawn = Math.round(offsetFromSpawn.length());
			final String directionFromSpawn = Utils.yawToDirection(Utils.vectorToYaw(offsetFromSpawn));
			distances.add(unitsFromSpawn + "m " + directionFromSpawn + " from the spawn");
			
			if(!Double.isInfinite(location.getX())){
				final Vector offsetFromYou = targetLocation.toVector().subtract(location.toVector());
				final long unitsFromYou = Math.round(offsetFromYou.length());
				final String directionFromYou = Utils.yawToDirection(Utils.vectorToYaw(offsetFromYou));
				distances.add(unitsFromYou + "m " + directionFromYou + " from you");
			}
		}
		
		/*if(commandSender.hasPermission("mcadmin.who.warp") && playerLevel >= playerHelper.getPlayerLevel(target)){
			
		}*/
		
		if(!distances.isEmpty()){
			final StringBuilder sb = Utils.enumerateStrings(distances);
			sb.insert(0, "That's ");
			sb.append('.');
			playerHelper.sendDirectedMessage(commandSender, sb.toString());
		}
		
		if(commandSender.hasPermission("mcadmin.who.address") && playerLevel >= playerHelper.getPlayerLevel(target) && target.isOnline()){
			playerHelper.sendDirectedMessage(commandSender, "IP: " + playerHelper.getPlayerIP(target) + " (" + playerHelper.getPlayerHost(target) + ")");
		}
	}

}
