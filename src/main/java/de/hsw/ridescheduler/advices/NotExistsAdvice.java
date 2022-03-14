package de.hsw.ridescheduler.advices;

import de.hsw.ridescheduler.exceptions.NotExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotExistsAdvice {

    @ResponseBody
    @ExceptionHandler(NotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String BusStopAlreadyExistsAdvice(NotExistsException ex) {
        return ex.getMessage();
    }
}
