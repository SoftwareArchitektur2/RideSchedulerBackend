package de.hsw.ridescheduler.advices;

import de.hsw.ridescheduler.exceptions.BusStopAlreadyExistsException;
import de.hsw.ridescheduler.exceptions.EntityHasDependenciesException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EnityHasDependenciesAdvice {

    @ResponseBody
    @ExceptionHandler(EntityHasDependenciesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String EntityHasDependenciesAdvice(BusStopAlreadyExistsException ex) {
        return ex.getMessage();
    }
}
