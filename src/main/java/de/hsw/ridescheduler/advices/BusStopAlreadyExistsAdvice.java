package de.hsw.ridescheduler.advices;

import de.hsw.ridescheduler.exceptions.BusStopAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BusStopAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(BusStopAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String BusStopAlreadyExistsAdvice(BusStopAlreadyExistsException ex) {
        return ex.getMessage();
    }
}
