package de.hsw.ridescheduler.exceptions;


public class BusStopHasBusLinesException extends EntityHasDependenciesException {
    public BusStopHasBusLinesException(String names){
        super("Haltestelle", "Buslinien", names);
    }
}
