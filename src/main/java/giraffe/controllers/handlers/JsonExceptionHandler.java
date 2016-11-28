package giraffe.controllers.handlers;

import giraffe.domain.GiraffeException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Handle errors from Ajax requests
 *
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@EnableWebMvc
@ControllerAdvice/*(basePackages = {""})*/
@Order(1)
public class JsonExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public GiraffeException.ErrorResponse handleErrorJson(final Exception e) {

        GiraffeException.ErrorResponse errorResponse = new GiraffeException.ErrorResponse();

        errorResponse.setMessage(e.getMessage());
        if (e instanceof GiraffeException) errorResponse.setErrorCode(((GiraffeException) e).getErrorCode());

        return errorResponse;
    }
}
