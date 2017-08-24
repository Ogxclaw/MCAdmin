package com.kirik.zen.vanish.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.main.ZenCommandException;

@Names({"vanish", "v"})
@Permission("zen.admin.vanish")
public class VanishCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		boolean isVanished = playerConfig.getPlayerConfig().getBoolean("isVanished");
		
		if(isVanished){
			plugin.vanish.showPlayer(player);
			playerHelper.sendDirectedMessage(commandSender, "Unvanished.");
		}else{
			plugin.vanish.hidePlayer(player);
			playerHelper.sendDirectedMessage(commandSender, "Vanished.");
		}
	}

}
