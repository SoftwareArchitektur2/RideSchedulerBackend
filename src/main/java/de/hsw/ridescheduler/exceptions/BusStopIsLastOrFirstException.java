package de.hsw.ridescheduler.exceptions;

public class BusStopIsLastOrFirstException extends RuntimeException {

    public BusStopIsLastOrFirstException(String message) {
        super(message);
    }
}
