package de.hsw.ridescheduler.exceptions;

public class BusLineAlreadyExistsException extends AlreadyExistsException {
    public BusLineAlreadyExistsException(String name) {
        super(String.format("Die Buslinie %s existiert bereits.\nBitte wählen Sie einen eindeutigen Namen."));
    }
}
