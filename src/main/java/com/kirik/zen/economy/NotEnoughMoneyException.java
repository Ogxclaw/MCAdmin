package com.kirik.zen.economy;

import com.kirik.zen.main.ZenCommandException;

public class NotEnoughMoneyException extends ZenCommandException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotEnoughMoneyException(){
		super("You do not have enough money in your account!");
	}

}
