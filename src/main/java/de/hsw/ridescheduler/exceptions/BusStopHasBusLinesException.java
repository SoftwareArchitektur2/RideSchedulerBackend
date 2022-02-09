package de.hsw.ridescheduler.exceptions;


public class BusStopHasBusLinesException extends RuntimeException {
    public BusStopHasBusLinesException(String names){
        super(String.format("Die Haltestelle wird noch von folgenden Buslinien angefahren %d. \nLeider kann sie dann nicht gelöscht werden.", names));
    }
}
