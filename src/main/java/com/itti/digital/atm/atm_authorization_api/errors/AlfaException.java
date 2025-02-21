package com.itti.digital.atm.atm_authorization_api.errors;

public class AlfaException extends RuntimeException{
	
    public AlfaException(String message) {
        super(message);
    }

    public AlfaException(String message, Exception e) {
        super(message,e);
    }
	
}
