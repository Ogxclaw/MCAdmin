package com.kirik.mcadmin.core.util;

public class PermissionDeniedException extends MCAdminCommandException {
	
	public PermissionDeniedException(){
		super("Permission Denied!");
	}

}
