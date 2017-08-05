package com.kirik.mcadmin.bans;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.bans.listeners.BansPlayerListener;
import com.kirik.mcadmin.config.BansConfiguration;
import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.main.offlinebukkit.OfflinePlayer;

public class Bans {
	
	private MCAdmin plugin;
	
	private BansPlayerListener playerListener;
	
	public Bans(MCAdmin plugin){
		this.plugin = plugin;
		playerListener = new BansPlayerListener();
	}
	
	public void ban(final CommandSender from, final Player player, final String reason, final BanType type){
		if(type == BanType.TEMPORARY)
			return;
		ban(from, player, reason, type, 0, "");
	}
	
	public void ban(final CommandSender from, final Player ply, final String reason, final BanType type, final long duration, final String measure) {
		final String addr;
		if (ply instanceof OfflinePlayer) {
			addr = "";
		} else {
			addr = ply.getAddress().getAddress().getHostAddress();
		}
		ban(from, ply.getName(), ply.getUniqueId(), addr, reason, type, duration, measure);
	}

	public void ban(final CommandSender from, final String plyName, final UUID plyUUID, final String ip, final String reason, final BanType type) {
		if (type == BanType.TEMPORARY) return;
		ban(from, plyName, plyUUID, ip, reason, type, 0, "");
	}
	
	public void ban(final CommandSender from, final String _playerName, final UUID playerUUID, final String ip, final String reason, final BanType type, final long duration, final String measure){
		if(type == null)
			return;
		if(type == BanType.TEMPORARY)
			return;
		
		final String playerName;
		if(_playerName == null)
			playerName = plugin.playerHelper.getPlayerByUUID(playerUUID).getName();
		else
			playerName = _playerName;
		
		BansConfiguration bans = new BansConfiguration();
		bans.getBansConfig().set(playerUUID.toString() + ".name", playerName);
		bans.getBansConfig().set(playerUUID.toString() + ".isBanned", true);
		bans.getBansConfig().set(playerUUID.toString() + ".reason", reason);
		bans.saveBansConfig();
		
		/*new Thread(){
			public void run(){
				Ban newBan = new Ban();
				newBan.setUser(playerName, playerUUID);
				newBan.setAdmin(from.getName(), Utils.getCommandSenderUUID(from));
				newBan.setReason(reason);
				newBan.setType(type.getName());
				BansConfiguration.addBan(newBan);
				plugin.playerHelper.sendServerMessage(from.getName() + " banned " + playerName + " [Reason: " + reason + "]");
			}
		}.start();*/
	}

}
