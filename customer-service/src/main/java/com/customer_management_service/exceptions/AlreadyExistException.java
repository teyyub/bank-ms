package com.customer_management_service.exceptions;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String s) {
        super(s);
    }

    public AlreadyExistException(){
        super("This number already exists !!");
    }
}
