package com.kirik.zen.factions.commands;

import org.bukkit.command.CommandSender;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.main.ZenCommandException;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

@Names("lf")
@Permission("zen.lf")
public class ListFactionCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		MPlayer mPlayer = MPlayer.get(commandSender);
		Faction faction = mPlayer.getFaction();
		playerHelper.sendDirectedMessage(commandSender, faction.getName());
	}

}
