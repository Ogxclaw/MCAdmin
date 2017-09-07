package com.kirik.zen.fun;

public enum Suffixes {
	
	GOOD_TIMES("zen.suffix.good_times","\u00a75\u00a7mGood\u00a7r \u00a75Times", (short)10),
	VIP("zen.suffix.vip","\u00a79VIP", (short)11),
	VIPPLUS("zen.suffix.vipplus","\u00a72VIP+", (short)13),
	BAE_AF("zen.suffix.baeaf", "\u00a74bae af", (short)14),
	NOT_GAY("zen.suffix.notgay", "\u00a7cn\u00a76o\u00a7et \u00a7ag\u00a7ba\u00a79y", (short)0);

	private final String perm;
	private final String displayName;
	private short color;
	
	Suffixes(String perm, String displayName, short color){
		this.perm = perm;
		this.displayName = displayName;
		this.color = color;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public String getPerm(){
		return this.perm;
	}
	
	public short getColor() {
		return this.color;
	}
	
}
