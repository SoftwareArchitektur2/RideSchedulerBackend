package de.hsw.ridescheduler.advices;

import de.hsw.ridescheduler.exceptions.AlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String BusStopAlreadyExistsAdvice(AlreadyExistsException ex) {
        return ex.getMessage();
    }
}
