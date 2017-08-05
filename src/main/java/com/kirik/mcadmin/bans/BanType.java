package com.kirik.mcadmin.bans;

public enum BanType {
	
	TEMPORARY("temp"),
	PERMANENT("perm");
	
	private final String name;
	
	BanType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}
