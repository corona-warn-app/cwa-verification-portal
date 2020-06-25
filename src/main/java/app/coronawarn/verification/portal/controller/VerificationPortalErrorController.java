/*
 * Corona-Warn-App / cwa-verification-portal
 *
 * (C) 2020, T-Systems International GmbH
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package app.coronawarn.verification.portal.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller handling errors.
 */
@Controller
public class VerificationPortalErrorController implements ErrorController {

  @Value("${rateLimiting.seconds}")
  private long rateLimitingSeconds;

  /**
   * Error messages for the common problems like 'Not Found', 'Internal Error'
   * 'Forbidden' and 'Too Many Requests'.
   */
  private static final String ERROR_404 = "Die aufgerufene Seite konnte nicht gefunden werden.";
  private static final String ERROR_403 = "Der Benutzer kann nicht authentifiziert werden.";
  private static final String ERROR = "Es kann keine TeleTAN aufgrund eines internen Fehlers generiert werden.";
  private static final String SECONDS = " Sekunden.";
  private static final String ERROR_429 = "Die Zeitlimitierung für TeleTAN Anfragen ist aktiv, bitte warten Sie ";

  /**
   * The internal route to the portal error web site.
   */
  private static final String ROUTE_ERROR = "/error";

  /**
   * The html Thymeleaf template for the error web site.
   */
  private static final String TEMPLATE_ERROR = "error";

  /**
   * The Thymeleaf attribute used for displaying the error message.
   */
  private static final String ATTR_ERROR_MSG = "message";

  /**
   * The Web GUI page request showing an Error message.
   *
   * @param request the original request
   * @param model the thymleaf model to be filled with the error text
   * @return the error template name
   */
  @RequestMapping(value = ROUTE_ERROR, method = {RequestMethod.GET, RequestMethod.POST})
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());

      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        model.addAttribute(ATTR_ERROR_MSG, ERROR_404);
      } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
        model.addAttribute(ATTR_ERROR_MSG, ERROR_403);
      } else if (statusCode == HttpStatus.TOO_MANY_REQUESTS.value()) {
        model.addAttribute(ATTR_ERROR_MSG, ERROR_429 + rateLimitingSeconds + SECONDS);
      } else {
        model.addAttribute(ATTR_ERROR_MSG, ERROR);
      }
    }
    HttpSession session = request.getSession();
    if (session != null) {
      session.removeAttribute(VerificationPortalController.SESSION_ATTR_TELETAN);
    }
    return TEMPLATE_ERROR;
  }

  /**
   * Get the path for the custom error page.
   *
   * @return the custom error path
   */
  @Override
  public String getErrorPath() {
    return ROUTE_ERROR;
  }

}
