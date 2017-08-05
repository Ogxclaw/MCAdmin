package com.kirik.mcadmin.factions.commands;

import org.bukkit.command.CommandSender;

import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.main.MCAdminCommandException;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

@Names("lf")
@Permission("mcadmin.lf")
public class ListFactionCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		MPlayer mPlayer = MPlayer.get(commandSender);
		Faction faction = mPlayer.getFaction();
		playerHelper.sendDirectedMessage(commandSender, faction.getName());
	}

}
