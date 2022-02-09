package de.hsw.ridescheduler.exceptions;

public abstract class AlreadyExistsException extends RuntimeException {

    protected AlreadyExistsException(String message) {
        super(message);
    }
}
