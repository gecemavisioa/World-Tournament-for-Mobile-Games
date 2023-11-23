package com.example.demo.exception;

public class QueueException extends RuntimeException{

    private static final long serialVerisionUID = 1;

    public QueueException(String message) {
        super(message);
    }
}
