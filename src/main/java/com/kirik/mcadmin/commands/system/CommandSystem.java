package com.kirik.mcadmin.commands.system;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.core.util.MCAdminCommandException;
import com.kirik.mcadmin.core.util.PermissionDeniedException;
import com.kirik.mcadmin.core.util.PlayerHelper;
import com.kirik.mcadmin.core.util.Utils;

public class CommandSystem {
	private final MCAdmin plugin;
	private final Map<String, ICommand> commands = new HashMap<>();

	public CommandSystem(MCAdmin plugin) {
		this.plugin = plugin;
		plugin.commandSystem = this;
		scanCommands();
	}

	public void scanCommands() {
		commands.clear();
		scanCommands("com.kirik.mcadmin.core");
	}

	public void scanCommands(String packageName) {
		for (Class<? extends ICommand> commandClass : Utils.getSubClasses(ICommand.class, packageName)) {
			try {
				commandClass.newInstance();
			} catch (InstantiationException e) {
				//constructor is not default
				continue;
			} catch (IllegalAccessException e) {
				//class isnt public
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public void registerCommand(String name, ICommand command) {
		commands.put(name, command);
	}

	public Map<String, ICommand> getCommands() {
		return commands;
	}

	public boolean runCommand(CommandSender commandSender, String cmd, String argStr) {
		String args[];
		if (argStr.isEmpty()) {
			args = new String[0];
		} else {
			args = argStr.split(" +");
		}
		return runCommand(commandSender, cmd, args, argStr);
	}

	public boolean runCommand(CommandSender commandSender, String cmd, String[] args, String argStr) {
		if (commands.containsKey(cmd)) {
			final String playerName = commandSender.getName();
			final ICommand icmd = commands.get(cmd);
			try {
				if (needsLogging(commandSender, icmd)) {
					String logmsg = "MCAdmin Command: " + playerName + ": " + cmd + " " + argStr;
					plugin.log(logmsg);
				}
				icmd.run(commandSender, args, argStr, cmd);
			} catch (PermissionDeniedException e) {
				String logmsg = "MCAdmin Command denied: " + playerName + ": " + cmd + " " + argStr;
				plugin.log(logmsg);

				PlayerHelper.sendDirectedMessage(commandSender, e.getMessage(), e.getColor());
			} catch (MCAdminCommandException e) {
				PlayerHelper.sendDirectedMessage(commandSender, e.getMessage(), e.getColor());
			} catch (Exception e) {
				if (commandSender.hasPermission("mcadmin.detailederrors")) {
					PlayerHelper.sendDirectedMessage(commandSender, "Command error: " + e + " in " + e.getStackTrace()[0]);
					e.printStackTrace();
				} else {
					PlayerHelper.sendDirectedMessage(commandSender, "Command error!");
				}
			}
			return true;
		}
		return false;
	}

	private boolean needsLogging(CommandSender commandSender, ICommand command) {
		if (commandSender instanceof BlockCommandSender)
			return command.hasAbusePotential();

		return true;
	}

	public boolean runCommand(CommandSender commandSender, String baseCmd) {
		int posSpace = baseCmd.indexOf(' ');
		String cmd;
		String argStr;
		if (posSpace < 0) {
			cmd = baseCmd.toLowerCase();
			argStr = "";
		} else {
			cmd = baseCmd.substring(0, posSpace).trim().toLowerCase();
			argStr = baseCmd.substring(posSpace).trim();
		}
		return runCommand(commandSender, cmd, argStr);
	}
}