package giraffe.controllers.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 *
 * Invoked only for exception thrown in Spring Security filter chain
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

    @RequestMapping(value = PATH, produces = "text/html")
    public String errorHtml(HttpServletRequest request, final Model model) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, false);

        model.addAttribute("status", errorAttributes.get("status"));
        model.addAttribute("message", errorAttributes.get("message"));

        return ("error");
    }

}
