package app.coronawarn.verification.portal.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class VerificationPortalErrorController implements ErrorController {

    /**
     * Error messages for the comon two problems 'Not Found' and 'Internal Error'
     */
    private final static String ERROR_404 =  "Die aufgerufene Seite konnte nicht gefunden werden";
    private final static String ERROR_500 =  "Es konnte keine TeleTAN aufgrund eines internen Fehlers generiert werden.";

    /**
     * The internal route to the portal error web site
     */
    private static final String ROUTE_ERROR = "/error";

    /**
     * The html Thymeleaf template for theerror web site
     */
    private static final String TEMPLATE_ERROR = "error";

    /**
     * The Thymeleaf attribute used for displaying the error message
     */
    private static final String ATTR_ERROR_MSG = "message";

    @RequestMapping(ROUTE_ERROR)
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute(ATTR_ERROR_MSG, ERROR_404);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute(ATTR_ERROR_MSG, ERROR_500);
            }
        }
        return TEMPLATE_ERROR;
    }

    @Override
    public String getErrorPath() {
        return ROUTE_ERROR;
    }
}
