package com.dallotech.exception;

import lombok.Data;

@Data
public class BaseException extends Exception {
    private String message;
    private String suggestion;

    public BaseException(){
        super();
    }

    public BaseException(String message){
        super(message);
        this.message = message;
    }

    public BaseException(String message, String suggestion){
        super(message);
        this.message = message;
        this.suggestion = suggestion;
    }
}
