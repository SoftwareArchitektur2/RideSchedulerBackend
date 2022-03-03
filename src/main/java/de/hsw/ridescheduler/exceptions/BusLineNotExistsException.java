package de.hsw.ridescheduler.exceptions;

public class BusLineNotExistsException extends NotExistsException{
    public BusLineNotExistsException(Long id) {
        super(String.format("Die angegebene Buslinie %s existiert nicht.", id));
    }

    public BusLineNotExistsException(String name) {
        super(String.format("Die angegebene Buslinie %s existiert nicht.", name));
    }
}
