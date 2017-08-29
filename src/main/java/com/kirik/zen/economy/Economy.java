package com.kirik.zen.economy;

import org.bukkit.entity.Player;

import com.kirik.zen.core.Zen;

public class Economy {
	
	public Zen plugin;
	
	public Economy(Zen plugin){
		this.plugin = plugin;
	}
	
	public void payPlayer(Player player, Player target, int amt) throws NotEnoughMoneyException {
		subtractFromBalance(player, amt);
		addToBalance(target, amt);
	}
	
	public void setBalance(Player player, int amt){
		plugin.getEcoConfig().set(player.getUniqueId() + ".balance", amt);
		plugin.saveEcoConfig();
	}
	
	public int getBalance(Player player){
		return plugin.getEcoConfig().getInt(player.getUniqueId() + ".balance");
	}
	
	public void addToBalance(Player player, int amt){
		int currentBal = plugin.getEcoConfig().getInt(player.getUniqueId() + ".balance");
		plugin.getEcoConfig().set(player.getUniqueId() + ".balance", currentBal + amt);
		plugin.saveEcoConfig();
	}
	
	public void subtractFromBalance(Player player, int amt) throws NotEnoughMoneyException {
		int currentBal = plugin.getEcoConfig().getInt(player.getUniqueId() + ".balance");
		if(currentBal < amt){
			throw new NotEnoughMoneyException();
		}
		plugin.getEcoConfig().set(player.getUniqueId() + ".balance", currentBal - amt);
		plugin.saveEcoConfig();
	}

}
