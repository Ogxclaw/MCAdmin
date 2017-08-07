package com.kirik.zen.bans;

import java.time.LocalDate;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.bans.listeners.BansPlayerListener;
import com.kirik.zen.config.time.Days;
import com.kirik.zen.core.Zen;
import com.kirik.zen.main.offlinebukkit.OfflinePlayer;

public class Bans {
	
	private Zen plugin;
	
	@SuppressWarnings("unused")
	private BansPlayerListener playerListener;
	
	public Bans(Zen plugin){
		this.plugin = plugin;
		playerListener = new BansPlayerListener();
	}
	
	public void ban(final CommandSender from, final Player player, final String reason, final BanType type){
		if(type == BanType.TEMPORARY)
			return;
		ban(from, player, reason, type, "");
	}
	
	public void ban(final CommandSender from, final Player ply, final String reason, final BanType type, String duration) {
		final String addr;
		if (ply instanceof OfflinePlayer) {
			addr = "";
		} else {
			addr = ply.getAddress().getAddress().getHostAddress();
		}
		ban(from, ply.getName(), ply.getUniqueId(), addr, reason, type, duration);
	}

	public void ban(final CommandSender from, final String plyName, final UUID plyUUID, final String ip, final String reason, final BanType type) {
		if (type == BanType.TEMPORARY) return;
		ban(from, plyName, plyUUID, ip, reason, type, "");
	}
	
	public void offlineBan(final CommandSender from, final String playerName, final String UUID, final String reason, final BanType type, String duration){
		String uuid = plugin.getUUIDConfig().getString(playerName.toLowerCase() + ".uuid");
		
		if(type == BanType.TEMPORARY){
			LocalDate date = LocalDate.now();
			int year = date.getDayOfYear();
			
			int day1 = year;
			int day2 = Integer.parseInt(duration.substring(0, duration.length() - 1));
			plugin.getBansConfig().set(uuid + ".name", playerName);
			plugin.playerHelper.setUnbanDate(uuid, Days.addDays(day1, day2));
			plugin.playerHelper.banPlayer(uuid);
			plugin.playerHelper.setBanReason(uuid, reason);
			plugin.playerHelper.setBanType(uuid, type);
			plugin.saveBansConfig();
			
			plugin.playerHelper.sendServerMessage(from.getName() + " banned " + playerName + " for " + day2 + " d ((" + reason + "))");
			return;
		}
		
		plugin.getBansConfig().set(uuid + ".name", playerName);
		plugin.playerHelper.banPlayer(uuid);
		plugin.playerHelper.setBanReason(uuid, reason);
		plugin.playerHelper.setBanType(uuid, type);
		plugin.saveBansConfig(); 
		
		plugin.playerHelper.sendServerMessage(from.getName() + " banned " + playerName + " [Reason: " + reason + "]");
	}
	
	public void ban(final CommandSender from, final String _playerName, final UUID _playerUUID, final String ip, final String reason, final BanType type, String duration){
		if(type == null)
			return;
		
		final String playerName;
		if(_playerName == null)
			playerName = plugin.playerHelper.getPlayerByUUID(_playerUUID).getName();
		else
			playerName = _playerName;
		
		final String playerUUID = _playerUUID.toString();
		
		if(type == BanType.TEMPORARY){
			LocalDate date = LocalDate.now();
			int year = date.getDayOfYear();
			
			int day1 = year;
			int day2 = Integer.parseInt(duration.substring(0, duration.length() - 1));
			plugin.getBansConfig().set(playerUUID + ".name", playerName);
			plugin.playerHelper.setUnbanDate(playerUUID, Days.addDays(day1, day2));
			plugin.playerHelper.banPlayer(playerUUID);
			plugin.playerHelper.setBanReason(playerUUID, reason);
			plugin.playerHelper.setBanType(playerUUID, type);
			plugin.saveBansConfig();
			
			plugin.playerHelper.sendServerMessage(from.getName() + " banned " + playerName + " for " + day2 + "d ((" + reason + "))");
			return;
		}
		
		//BansConfiguration bans = new BansConfiguration();
		plugin.getBansConfig().set(playerUUID + ".name", playerName);
		plugin.playerHelper.banPlayer(playerUUID);
		plugin.playerHelper.setBanReason(playerUUID, reason);
		plugin.playerHelper.setBanType(playerUUID, type);
		plugin.saveBansConfig();
		
		plugin.playerHelper.sendServerMessage(from.getName() + " banned " + playerName + " ((" + reason + "))");
	}
	
	public boolean isBanned(String uuid){
		return plugin.getBansConfig().getBoolean(uuid + ".isBanned");
	}
	
}
