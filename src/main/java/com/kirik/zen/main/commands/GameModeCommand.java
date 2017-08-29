package com.kirik.zen.main.commands;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names({"gamemode", "gm"})
@Usage("/gm <s|c|a|sp|0|1|2|3> [name]")
@Help("Sets the gamemode of one of you fucks")
@Permission("zen.gamemode")
public class GameModeCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 0)
			throw new ZenCommandException(this.getUsage());
		String gamemode = args[0];
		Player player = (Player)commandSender;
		GameMode playerGamemode = player.getGameMode();
		switch(gamemode){
		case "0":
		case "s":
			playerGamemode = GameMode.SURVIVAL;
			break;
		case "1":
		case "c":
			playerGamemode = GameMode.CREATIVE;
			break;
		case "2":
		case "a":
			playerGamemode = GameMode.ADVENTURE;
			break;
		case "3":
		case "sp":
			playerGamemode = GameMode.SPECTATOR;
			break;
		}
		if(args.length == 1){
			player.setGameMode(playerGamemode);
			playerHelper.sendDirectedMessage(commandSender, "Gamemode set to " + playerGamemode.toString());
			return;
		}
		if(args.length == 2 && commandSender.hasPermission("zen.gamemode.others")){
			Player target = playerHelper.matchPlayerSingle(args[1]);
			playerHelper.sendDirectedMessage(commandSender, "Set gamemode of " + target.getName() + " to " + playerGamemode.toString());
			return;
		}
	}

}
