package com.kirik.mcadmin.main;

public class MCAdminCommandException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private char color = '5';
	
	public MCAdminCommandException(String message){
		super(message);
	}
	
	public MCAdminCommandException(Throwable cause){
		super(cause);
	}
	
	public MCAdminCommandException(String message, Throwable cause){
		super(message, cause);
	}
	
	public MCAdminCommandException setColor(char color){
		this.color = color;
		return this;
	}
	
	public char getColor(){
		return color;
	}

}
