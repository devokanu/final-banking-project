package com.okan.bankingmanagement.constant;

public class Authority {
	
	
	public static final String USER_AUTHORITIES =  "user:read" ;
    public static final String MANAGER_AUTHORITIES = "user:read , user: ACTIVATE_DEACTIVATE_USER ,user:CREATE_ACCOUNT, user:REMOVE_ACCOUNT" ;
    public static final String ADMIN_AUTHORITIES = "user:read , user:CREATE_BANK ,"
    		+ " user: ACTIVATE_DEACTIVATE_USER ,user:CREATE_ACCOUNT, user:REMOVE_ACCOUNT" ;
    
    
    
    
}
