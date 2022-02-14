package de.hsw.ridescheduler.exceptions;

public class ScheduleNotExistsException extends NotExistsException {
    public ScheduleNotExistsException(Long id) {
        super(String.format("Der angegebene Fahrplan %s existiert nicht.", id));
    }
}
