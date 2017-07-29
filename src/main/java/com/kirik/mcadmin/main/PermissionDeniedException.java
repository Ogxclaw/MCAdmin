package com.kirik.mcadmin.main;

public class PermissionDeniedException extends MCAdminCommandException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionDeniedException(){
		super("Permission Denied!");
	}

}
