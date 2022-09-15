package com.example.spgtu.exceptions;

public class CommandNotFoundException extends Exception {

    public CommandNotFoundException(Exception e) {
        super(e);
    }

    public CommandNotFoundException() {

    }
}
