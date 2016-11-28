package giraffe.controllers.security;

import giraffe.domain.GiraffeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * Invoked only for exception thrown in Spring Security filter chain
 *
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Controller
public class GiraffeErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    private static final String PATH = "/error";


    @Autowired
    public GiraffeErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    @RequestMapping(value = PATH, produces = "application/json")
    @ResponseBody
    public GiraffeException.ErrorResponse handleErrorJson(HttpServletRequest request, final Model model) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, false);

        GiraffeException.ErrorResponse errorResponse = new GiraffeException.ErrorResponse();
        errorResponse.setMessage(errorAttributes.get("message").toString());

        return errorResponse;
    }

}
