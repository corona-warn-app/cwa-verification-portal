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

    private final static String ERROR_404 =  "Die aufgerufene Seite konnte nicht gefunden werden";
    private final static String ERROR_500 =  "Es konnte keine TeleTAN aufgrund eines internen Fehlers generiert werden.";

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", ERROR_404);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", ERROR_500);
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
