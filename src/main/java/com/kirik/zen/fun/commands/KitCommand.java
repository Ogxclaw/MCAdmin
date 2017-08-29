package com.kirik.zen.fun.commands;

import java.time.LocalDate;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.main.ZenCommandException;

@Names("kit")
@Usage("/kit <kitName>")
@Help("Gives player a kit")
@Permission("zen.kit")
public class KitCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1){
			throw new ZenCommandException(this.getUsage());
		}
		Player player = (Player)commandSender;
		String kitName = args[0].toLowerCase();
		final PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		LocalDate date = LocalDate.now();
		//int dayOfYear = date.getDayOfYear();
		switch(kitName){
		case "daily":
			if(playerConfig.getPlayerConfig().getBoolean("dailyTimer")){
				playerHelper.sendDirectedMessage(commandSender, "You have already used your daily kit!");
				break;
			}
			playerConfig.getPlayerConfig().set("dailyTimer", true);
			playerConfig.savePlayerConfig();
			new BukkitRunnable(){
				@Override
				public void run(){
					playerConfig.getPlayerConfig().set("dailyTimer", false);
					playerConfig.savePlayerConfig();
					playerHelper.sendDirectedMessage(commandSender, "Done");
				}
			}.runTaskLaterAsynchronously(plugin, 1200);
			player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE, 1));
			player.getInventory().addItem(new ItemStack(Material.STONE_AXE, 1));
			player.getInventory().addItem(new ItemStack(Material.STONE_SPADE, 1));
			player.getInventory().addItem(new ItemStack(Material.STONE_SWORD, 1));
			player.getInventory().addItem(new ItemStack(Material.WOOD, 32));
			player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 32));
			player.getInventory().addItem(new ItemStack(Material.GRILLED_PORK, 4));
		}
	}

}
