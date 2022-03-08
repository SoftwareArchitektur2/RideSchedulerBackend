package de.hsw.ridescheduler.exceptions;

public class BusLineHasSchedulesException extends EntityHasDependenciesException {
    public BusLineHasSchedulesException(String names) {
        super("BusLinie", "Fahrpläne", names);
    }
}
