package com.oop.lab.first.firstlab.exception;

public class ResourceAlreadyExistsException extends Exception {
 
    public ResourceAlreadyExistsException() {
    }
 
    public ResourceAlreadyExistsException(String msg) {
        super(msg);
    }
}