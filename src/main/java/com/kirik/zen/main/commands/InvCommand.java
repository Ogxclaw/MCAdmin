package com.kirik.zen.main.commands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.main.ZenCommandException;

@Names("inv")
@Permission("zen.no")
public class InvCommand extends ICommand {
	
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Inventory customInv = plugin.getServer().createInventory(null, 9, "Custom Inv");
		
		ItemStack redShulker = new ItemStack(Material.WOOL, 1);
		ItemMeta redMeta = redShulker.getItemMeta();
		redMeta.setDisplayName("Sell");
		redShulker.setItemMeta(redMeta);
		customInv.setItem(0, redShulker);
		ItemStack greenShulker = new ItemStack(Material.GREEN_SHULKER_BOX, 1);
		ItemMeta greenMeta = redShulker.getItemMeta();
		greenMeta.setDisplayName("Buy");
		greenShulker.setItemMeta(greenMeta);
		customInv.setItem(8, greenShulker);
		
		Player player = (Player)commandSender;
		foo(player);
		//player.openInventory(customInv);
		
	}
	
	public void foo(Player p) {
	     plugin.signMenu.open(p, new String[]{"", "^^^^^^^^", "Enter a price!", ""}, (player1, text) -> Arrays.stream(text).forEach(player1::sendMessage));
	}
 
}
