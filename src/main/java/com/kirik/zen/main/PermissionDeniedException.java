package com.kirik.zen.main;

public class PermissionDeniedException extends ZenCommandException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionDeniedException(){
		super("Permission Denied!");
	}

}
