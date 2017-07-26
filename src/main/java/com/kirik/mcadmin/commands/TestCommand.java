package com.kirik.mcadmin.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.commands.BaseCommand.Name;
import com.kirik.mcadmin.core.util.MCAdminCommandException;

@Name("test")
public class TestCommand extends BaseCommand {
	
	@Override
	public boolean onCommandPlayer(Player player, Command command, String s, String[] strings) throws MCAdminCommandException {
		player.sendMessage("\u00a75[MCAdmin] \u00a7fThis should work");
		return true;
	}

}
