package ru.cft.service.argument.exception;

public class DuplicateArgumentNameException extends Exception {
    public DuplicateArgumentNameException(String argumentName) {
        super(argumentName + " argument name is specified multiple times");
    }
}
