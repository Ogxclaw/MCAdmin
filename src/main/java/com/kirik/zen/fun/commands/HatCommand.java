package com.kirik.zen.fun.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("hat")
@Usage("/hat")
@Help("Places the block you are \ncurrently holding on your head!")
@Permission("zen.vip.hat")
public class HatCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		ItemStack itemPrevOnHead = player.getInventory().getHelmet();
		player.getInventory().addItem(itemPrevOnHead);
		//player.getInventory().removeItem(itemPrevOnHead);
		player.getInventory().setHelmet(itemInHand);
		player.getInventory().removeItem(itemInHand);
		playerHelper.sendDirectedMessage(commandSender, "Hat set!");
	}
}
