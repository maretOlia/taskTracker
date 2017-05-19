package giraffe.config;

import giraffe.domain.GiraffeException;
import giraffe.handlers.JsonExceptionHandler;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(1)
public class TrackerJsonExceptionHandler extends JsonExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoActivityWithCurrentUuidException.class})
    @ResponseBody
    public GiraffeException.ErrorResponse handleNotFoundJson(Exception e) {

        GiraffeException.ErrorResponse errorResponse = new GiraffeException.ErrorResponse();

        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorCode(((GiraffeException) e).getErrorCode());

        return errorResponse;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({GiraffeAccessDeniedException.class})
    @ResponseBody
    public GiraffeException.ErrorResponse handleForbiddenJson(Exception e) {

        GiraffeException.ErrorResponse errorResponse = new GiraffeException.ErrorResponse();

        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorCode(((GiraffeException) e).getErrorCode());

        return errorResponse;
    }

}
