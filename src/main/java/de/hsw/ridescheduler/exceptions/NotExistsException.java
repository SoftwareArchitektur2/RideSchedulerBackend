package de.hsw.ridescheduler.exceptions;

public abstract class NotExistsException extends RuntimeException{

    protected NotExistsException(String message){
        super(message);
    }
}
