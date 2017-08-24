package com.kirik.zen.main.offlinebukkit;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;

public class OfflinePlayer extends AbstractPlayer {
	
	private World world;
	
	public OfflinePlayer(Server server, String name){
		this(server, null, name);
	}
	
	public OfflinePlayer(Server server, UUID uuid){
		this(server, uuid, null);
	}
	
	public void sendSignChange(Location loc, String[] str){
		
	}
	
	public OfflinePlayer(Server server, UUID uuid, String name){
		super((CraftServer)server, uuid, name);
		
		//File playerFile = PlayerHelper.getPlayerFile(this.getUniqueId(), "world");
		
		world = server.getWorld("world"); //default val
		world.getSpawnLocation();
		
		//MCAdmin.instance.playerHelper.setPlayerDisplayName(this);
		
		/*if(playerFile == null || !playerFile.exists())
			return;*/
		
	}

	

}
