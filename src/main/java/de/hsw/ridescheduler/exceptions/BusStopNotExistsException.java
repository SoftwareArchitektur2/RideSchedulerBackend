package de.hsw.ridescheduler.exceptions;

public class BusStopNotExistsException extends RuntimeException {
    public BusStopNotExistsException(Long id){
        super(String.format("Zu der ID %s existiert keine Haltestelle. \nVersuchen sie es mit einer anderen ID.", id));
    }

}
