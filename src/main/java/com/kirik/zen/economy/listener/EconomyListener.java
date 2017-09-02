package com.kirik.zen.economy.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.economy.Economy;
import com.kirik.zen.economy.NotEnoughMoneyException;
import com.kirik.zen.main.listeners.BaseListener;

public class EconomyListener extends BaseListener {
	
	@EventHandler
	public void onLogin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		/*int playerBalance = plugin.getEcoConfig().getInt(player.getUniqueId() + ".balance");*/
		if(!playerConfig.getPlayerConfig().getBoolean("ecoActive")){
			if(plugin.eco.getBalance(player) == 0){
				plugin.eco.addToBalance(player, 100);
			}
			playerConfig.getPlayerConfig().set("ecoActive", true);
			playerConfig.savePlayerConfig();
		}
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e){
		Economy econ = new Economy(plugin);
		if(e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST){
			Sign s = (Sign)e.getClickedBlock().getState();
			String[] text = s.getLines();
			if(text[0].equals(ChatColor.DARK_PURPLE + "[ZenShop]")){
				Material mat = Material.getMaterial(text[1].toUpperCase());
				String[] prices = text[2].split(":");
				int[] price = {Integer.parseInt(prices[0]), Integer.parseInt(prices[1])};
				for(int i : price){
					if(i < 0){
						playerHelper.sendDirectedMessage(e.getPlayer(), "This shop isn't configured correctly, please contact an admin/manager.");
						return;
					}
				}
				int amount = Integer.parseInt(text[3]);
				Player player = e.getPlayer();
				if(e.getAction() == Action.LEFT_CLICK_BLOCK){
					//buy action
					if(econ.getBalance(player) >= price[0]){
						player.getInventory().addItem(new ItemStack(mat, amount));
						try {
							econ.subtractFromBalance(player, price[0]);
						} catch (NotEnoughMoneyException e1) {
							playerHelper.sendDirectedMessage(player, e1.getMessage());
						}
						playerHelper.sendDirectedMessage(player, mat + " bought for " + price[0] + "gp");
					}else{
						playerHelper.sendDirectedMessage(player, "Not enough money");
					}
				}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
					//sell action
					if(contains(player, mat, amount)){
						remove(player, new ItemStack(mat, amount));
						econ.addToBalance(player, price[1]);
						playerHelper.sendDirectedMessage(player, mat + " sold for " + price[1] + "gp");
					}else{
						playerHelper.sendDirectedMessage(player, "You don't have enough items to sell.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		Player player = e.getPlayer();
		String[] text = e.getLines();
		if(text[0].equals("[ZenShop]")){
			if(player.hasPermission("zen.eco.makeshop")){
				e.setLine(0, ChatColor.DARK_PURPLE + "[ZenShop]");
				e.getBlock().getState().update();
			}else{
				playerHelper.sendDirectedMessage(player, "Permission Denied!");
			}
		}
	}
	
	private boolean contains(Player player, Material mat, int amount){
		int count = 0;
		for(int i = 0; i < player.getInventory().getSize(); i++){
			if(player.getInventory().getItem(i) != null){
				if(player.getInventory().getItem(i).getType() == mat){
					count += player.getInventory().getItem(i).getAmount();
					if(count >= amount){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void remove(Player player, ItemStack itemStack){
		int amount = itemStack.getAmount();
		int count = 0;
		Material mat = itemStack.getType();
		for(int i = 0; i < player.getInventory().getSize() && count < amount; i++){
			ItemStack tmp = player.getInventory().getItem(i);
			if(tmp != null){
				if(tmp.getType() == mat){
					if(tmp.getAmount() >= amount - count){
						tmp.setAmount(tmp.getAmount() - (amount - count));
						player.getInventory().setItem(i, tmp);
						return;
					}else if(tmp.getAmount() < amount - count){
						count += tmp.getAmount();
						player.getInventory().getItem(i).setAmount(0);
					}
				}
			}
		}
	}

}
