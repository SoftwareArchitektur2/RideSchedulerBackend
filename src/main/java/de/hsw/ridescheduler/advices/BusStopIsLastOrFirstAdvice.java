package de.hsw.ridescheduler.advices;

import de.hsw.ridescheduler.exceptions.BusStopIsLastOrFirstException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BusStopIsLastOrFirstAdvice {

    @ResponseBody
    @ExceptionHandler(BusStopIsLastOrFirstException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusStopIsLastOrFirstException(BusStopIsLastOrFirstException ex) {
        return ex.getMessage();
    }
}
