package giraffe.controllers.handlers;

import giraffe.domain.GiraffeException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
* @author Guschcyna Olga
* @version 1.0.0
*/
@ControllerAdvice
@Order(1)
public class GiraffeExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({GiraffeException.class})
    public String handleErrorHtml(final GiraffeException e, final Model model) {

        model.addAttribute("message", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode());

        return ("error");
    }

}
