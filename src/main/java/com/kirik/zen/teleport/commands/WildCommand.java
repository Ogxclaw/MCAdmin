package com.kirik.zen.teleport.commands;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.main.ZenCommandException;

@Names("wild")
@Help("Teleports user to random location in the wild")
@Permission("zen.teleport.wild")
public class WildCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		double borderSize = plugin.getServer().getWorld("world").getWorldBorder().getSize();
		Random randXCoord = new Random();
		Random randZCoord = new Random();
		
		Player player = (Player)commandSender;
		int xCoord = randXCoord.nextInt((int)borderSize) + 1;
		int zCoord = randZCoord.nextInt((int)borderSize) + 1;
		while(xCoord > (borderSize / 2)){
			xCoord = randXCoord.nextInt((int)borderSize) + 1;
		}
		while(zCoord > (borderSize / 2)){
			zCoord = randZCoord.nextInt((int)borderSize) + 1;
		}
		while(xCoord + zCoord > borderSize){
			xCoord = randXCoord.nextInt((int)borderSize) + 1;
			zCoord = randZCoord.nextInt((int)borderSize) + 1;
		}
		int yCoord = playerHelper.getSolidBlock(xCoord, zCoord, player);
		//playerHelper.hasBlockAirAboveHead(player);
		//playerHelper.hasBlockBelow(player);
		player.teleport(new Location(player.getLocation().getWorld(), xCoord, yCoord, zCoord, 0.0F, 0.0F));
		playerHelper.sendDirectedMessage(player, "Teleported to a random location!");
	}

}
