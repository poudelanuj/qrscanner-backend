package com.anuj.qrscanner.exception;



public class UserAuthenticationFailedException extends BaseException {
    public UserAuthenticationFailedException(String msg, String suggestion) { super(msg,suggestion); }

    public UserAuthenticationFailedException(String s) {
        super(s);
    }
    public UserAuthenticationFailedException(){
        super("Could not authenticate the request","Retry login and continue.");
    }


}
