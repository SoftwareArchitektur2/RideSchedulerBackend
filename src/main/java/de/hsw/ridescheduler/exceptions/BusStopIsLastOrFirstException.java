package de.hsw.ridescheduler.exceptions;

public class BusStopIsLastOrFirstException extends RuntimeException {

    public BusStopIsLastOrFirstException(String nameBusStop, String nameBusLine) {
        super(String.format("Die Haltestelle %s ist die erste oder die letzte Haltestelle der Buslinie %s. \nUm die Haltestelle zu löschen, muss eine neue Zielhaltestelle der für die Buslinie vergeben werden.", nameBusStop, nameBusLine));
    }
}
