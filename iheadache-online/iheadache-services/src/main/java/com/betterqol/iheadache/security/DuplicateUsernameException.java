package com.betterqol.iheadache.security;

public class DuplicateUsernameException extends RuntimeException {
	
	public DuplicateUsernameException(String msg) {
		
		super(msg);
	}

}
