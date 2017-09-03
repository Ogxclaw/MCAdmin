package com.kirik.zen.warps;

import org.bukkit.Location;

public class Home {
	
	private String name;
	private Location playerLocation;
	private float playerPitch;
	private float playerYaw;
	
	public Home(String name, Location playerLocation, float playerPitch, float playerYaw){
		this.playerLocation = playerLocation;
		this.playerPitch = playerPitch;
		this.playerYaw = playerYaw;
		
		playerLocation.setPitch(playerPitch);
		playerLocation.setYaw(playerYaw);
	}
	
	public Home(Location playerLocation, float playerPitch, float playerYaw){
		this("default", playerLocation, playerPitch, playerYaw);
	}
	
	public String getHomeName(){
		if(name == null)
			return "default";
		else
			return name;
	}
	
	public Location getPlayerLocation(){
		return playerLocation;
	}
	
	public float getPlayerPitch(){
		return playerPitch;
	}
	
	public float getPlayerYaw(){
		return playerYaw;
	}

}
