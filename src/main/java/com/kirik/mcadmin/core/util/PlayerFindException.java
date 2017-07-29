package com.kirik.mcadmin.core.util;

import com.kirik.mcadmin.main.MCAdminCommandException;

public class PlayerFindException extends MCAdminCommandException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerFindException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlayerFindException(String message) {
		super(message);
	}

	public PlayerFindException(Throwable cause) {
		super(cause);
	}

}
