package com.example.demo.exception;

public class ForbiddenException extends RuntimeException{

    private static final long serialVerisionUID = 1;

    public ForbiddenException(String message) {
        super(message);
    }
}
