package com.kirik.zen.chat.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.fun.Suffixes;
import com.kirik.zen.main.ZenCommandException;

@Names("suffix")
@Help("Lets you choose a suffix")
@Usage("/suffix [suffix|list]")
@Permission("zen.suffix")
public class SuffixCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Player player = (Player)commandSender;
		int invSize = Suffixes.values().length + 1;
		while(invSize % 9 != 0) {
			invSize++;
		}
		Inventory suffixInv = player.getServer().createInventory(player, invSize, "Available Suffixes");
		//int size = Suffixes.values().length;
		
		/*ItemStack goodTimesSuffix = new ItemStack(Material.WOOL, 1, (short)10);
		ItemMeta goodTimesSuffixMeta = goodTimesSuffix.getItemMeta();
		String name = Suffixes.GOOD_TIMES.getDisplayName();
		goodTimesSuffixMeta.setDisplayName(name);
		List<String> available = new ArrayList<String>();
		available.add("\u00a77Available: " + (isSuffixAvailable(player, Suffixes.GOOD_TIMES) ? "\u00a7aYES" : "\u00a7cNO"));
		goodTimesSuffixMeta.setLore(available);
		goodTimesSuffix.setItemMeta(goodTimesSuffixMeta);
		suffixInv.setItem(0, goodTimesSuffix);*/
		
		int i = 0;
		for(Suffixes suffix : Suffixes.values()) {
			ItemStack item = new ItemStack(Material.CONCRETE, 1, suffix.getColor());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(suffix.getDisplayName());
			List<String> lore = new ArrayList<String>();
			lore.add("\u00a77Available: " + (isSuffixAvailable(player, suffix) ? "\u00a7aYES" : "\u00a7cNO"));
			meta.setLore(lore);
			item.setItemMeta(meta);
			suffixInv.setItem(i, item);
			i+=1;
		}
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("\u00a7cReset Tag");
		List<String> lore = new ArrayList<String>();
		lore.add("\u00a77Removes your current suffix.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		suffixInv.setItem(invSize - 1, item);
		
		player.openInventory(suffixInv);
	}
	
	public boolean isSuffixAvailable(Player player, Suffixes suffix) {
		if(player.hasPermission(suffix.getPerm())) {
			return true;
		}else {
			return false;
		}
	}
}
