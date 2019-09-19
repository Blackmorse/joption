package com.blackmorse.joption.exceptions;

public class CommandValidationException extends RuntimeException {
    public CommandValidationException(String msg) {
        super(msg);
    }

    public CommandValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
