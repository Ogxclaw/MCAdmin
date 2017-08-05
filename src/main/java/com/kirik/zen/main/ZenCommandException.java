package com.kirik.zen.main;

public class ZenCommandException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private char color = '5';
	
	public ZenCommandException(String message){
		super(message);
	}
	
	public ZenCommandException(Throwable cause){
		super(cause);
	}
	
	public ZenCommandException(String message, Throwable cause){
		super(message, cause);
	}
	
	public ZenCommandException setColor(char color){
		this.color = color;
		return this;
	}
	
	public char getColor(){
		return color;
	}

}
