package com.oop.lab.first.firstlab.exception;

public class ResourceNotFoundException extends Exception {
 
    public ResourceNotFoundException() {
    }
 
    public ResourceNotFoundException(String msg) {
        super(msg);
    }    
}