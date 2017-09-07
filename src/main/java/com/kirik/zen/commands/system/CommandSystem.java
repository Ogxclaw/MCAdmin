package com.kirik.zen.commands.system;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

import com.kirik.zen.core.Zen;
import com.kirik.zen.core.util.PlayerHelper;
import com.kirik.zen.core.util.Utils;
import com.kirik.zen.main.ZenCommandException;
import com.kirik.zen.main.PermissionDeniedException;

public class CommandSystem {
	private final Zen plugin;
	private PlayerHelper playerHelper;
	private final Map<String, ICommand> commands = new HashMap<>();

	public CommandSystem(Zen plugin) {
		this.plugin = plugin;
		plugin.commandSystem = this;
		playerHelper = plugin.playerHelper;
		scanCommands();
	}

	public void scanCommands() {
		commands.clear();
		scanCommands("com.kirik.zen.chat");
		scanCommands("com.kirik.zen.permissions");
		scanCommands("com.kirik.zen.main");
		scanCommands("com.kirik.zen.bans");
		scanCommands("com.kirik.zen.jail");
		//scanCommands("com.kirik.zen.factions");
		scanCommands("com.kirik.zen.teleport");
		scanCommands("com.kirik.zen.warps");
		scanCommands("com.kirik.zen.vanish");
		scanCommands("com.kirik.zen.fun");
		scanCommands("com.kirik.zen.economy");
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
				if(!icmd.canPlayerUseCommand(commandSender))
					throw new PermissionDeniedException();
				if (needsLogging(commandSender, icmd)) {
					String logmsg = "Zen Command: " + playerName + ": " + cmd + " " + argStr;
					plugin.logToConsole(logmsg);
				}
				
				icmd.run(commandSender, args, argStr, cmd);
			} catch (PermissionDeniedException e) {
				String logmsg = "Zen Command denied: " + playerName + ": " + cmd + " " + argStr;
				plugin.logErrorToConsole(logmsg);

				playerHelper.sendDirectedMessage(commandSender, e.getMessage(), e.getColor());
			} catch (ZenCommandException e) {
				playerHelper.sendDirectedMessage(commandSender, e.getMessage(), e.getColor());
			} catch (Exception e) {
				if (commandSender.hasPermission("zen.detailederrors")) {
					plugin.playerHelper.sendDirectedMessage(commandSender, "Command error: " + e + " in " + e.getStackTrace()[0]);
					e.printStackTrace();
				} else {
					plugin.playerHelper.sendDirectedMessage(commandSender, "Command error!");
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