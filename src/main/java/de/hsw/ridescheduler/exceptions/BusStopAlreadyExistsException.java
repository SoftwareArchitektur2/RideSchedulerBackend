package de.hsw.ridescheduler.exceptions;

public class BusStopAlreadyExistsException extends RuntimeException {
    public BusStopAlreadyExistsException(String name) {
        super(String.format("Die Haltestelle %s existiert bereits.\nBitte wählen Sie einen eindeutigen Namen.", name));
    }
}
