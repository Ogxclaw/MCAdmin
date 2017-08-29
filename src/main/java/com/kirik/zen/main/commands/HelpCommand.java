package com.kirik.zen.main.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names({"zenhelp"})
@Help("Prints a list of available commands or \ninformation about the specified command.")
@Usage("/help [commandName]")
@Permission("zen.help")
public class HelpCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		Map<String, ICommand> commands = plugin.commandSystem.getCommands();
		
		if(args.length > 0){
			ICommand val = commands.get(args[0]);
			if(val == null || !val.canPlayerUseCommand(commandSender)){
				throw new ZenCommandException("Command not found!");
			}
			for(String line : val.getHelp().split("\n")){
				playerHelper.sendDirectedMessage(commandSender, line);
			}
			playerHelper.sendDirectedMessage(commandSender, "Usage: /" + args[0] + " " + val.getUsage());
		}else{
			
			/*ItemStack redShulker = new ItemStack(Material.WOOL, 1, (byte)4);
			ItemMeta redMeta = redShulker.getItemMeta();
			redMeta.setDisplayName("Sell");
			redShulker.setItemMeta(redMeta);
			customInv.setItem(0, redShulker);
			ItemStack greenShulker = new ItemStack(Material.WOOL, 1, (byte)5);
			ItemMeta greenMeta = redShulker.getItemMeta();
			greenMeta.setDisplayName("Buy");
			greenShulker.setItemMeta(greenMeta);
			customInv.setItem(8, greenShulker);*/
			
			/*Player player = (Player)commandSender;
			String ret = "Available commands: /";;
			for(String key : new PriorityQueue<>(commands.keySet())){
				if(key == "\u00a7")
					continue;
				
				ICommand val = commands.get(key);
				if(!val.canPlayerUseCommand(commandSender))
					continue;
				
				
				ret += key + ", /";
			}
			ret = ret.substring(0, ret.length() - 3);
			playerHelper.sendDirectedMessage(commandSender, ret);*/
			
			Player player = (Player)commandSender;
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			String ret = "Available commands: /";
			int i = 0;
			int woolVal = 10;
			for(String key : new PriorityQueue<>(commands.keySet())){
				if(key == "\u00a7")
					continue;
				
				ICommand val = commands.get(key);
				if(!val.canPlayerUseCommand(commandSender))
					continue;
				
				stacks.add(new ItemStack(Material.STAINED_GLASS, 1, (byte)woolVal));
				if(woolVal == 10){
					woolVal = 14;
				}else{
					woolVal = 10;
				}
				
				ItemStack stack = stacks.get(i);
				ItemMeta stackMeta = stack.getItemMeta();
				stackMeta.setDisplayName("\u00a7a" + key);
				stackMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
				List<String> loreList = new ArrayList<String>();
				for(String line : val.getHelp().split("\n")){
					loreList.add("\u00a7f" + line);
				}
				loreList.add("\u00a7c" + val.getUsage());
				stackMeta.setLore(loreList);
				stack.setItemMeta(stackMeta);
				/*helpInv.setItem(i, stack);*/
				
				ret += key + ", /";
				i += 1;
			}
			while(i % 9 != 0){
				i += 1;
			}
			ret = ret.substring(0, ret.length() - 3);
			playerHelper.sendDirectedMessage(commandSender, ret);
			Inventory helpInv = plugin.getServer().createInventory(null, i, "\u00a75[Zen] \u00a78Help Menu");
			for(ItemStack stack : stacks){
				helpInv.addItem(stack);
			}
			player.openInventory(helpInv);
		}
	}
}
