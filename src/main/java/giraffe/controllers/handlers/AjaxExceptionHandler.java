package giraffe.controllers.handlers;

import giraffe.domain.GiraffeException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 *
 * Handle errors from Ajax requests  in future
 */
@ControllerAdvice/*(basePackages = {""})*/
@Order(3)
public class AjaxExceptionHandler {

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
