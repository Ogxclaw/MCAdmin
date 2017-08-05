package com.kirik.zen.core.util;

import com.kirik.zen.main.ZenCommandException;

public class PlayerFindException extends ZenCommandException {
	
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
