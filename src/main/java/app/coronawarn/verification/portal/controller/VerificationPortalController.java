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


import app.coronawarn.verification.portal.client.TeleTan;
import app.coronawarn.verification.portal.client.TeleTanClientSI;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class represents the WEB UI controller for the verification portal.
 * It implements a very simple HTML interface with one submit button to get and show a newly generated TeleTAN
 */
@Slf4j
@Controller
public class VerificationPortalController {

  /**
   * Session attribute showing that the index template has been shown already at least once in the current session
   * (means that now the teletan template should be shown instead of the index template)
   */
  static final String SESSION_ATTR_TELETAN = "teletan";

  /**
   * The route to the TeleTAN portal web site.
   */
  public static final String ROUTE_INDEX = "/index";

  /**
   * The route to the TeleTAN portal web site.
   */
  public static final String ROUTE_TELETAN = "/teletan";

  /**
   * The route to log out from the portal web site
   */
  private static final String ROUTE_LOGOUT = "/logout";

  /**
   * The html Thymeleaf template for the TeleTAN portal web site.
   */
  private static final String TEMPLATE_TELETAN = "teletan";

  /**
   * The html Thymeleaf template for the TeleTAN portal web site.
   */
  private static final String TEMPLATE_INDEX = "index";

  /**
   * The Thymeleaf attributes used for displaying the teletan and the current user
   */
  private static final String ATTR_TELETAN = "teleTAN";
  private static final String ATTR_USER = "userName";

  /**
   * The REST client interface for getting the TeleTAN from verificationserver.
   */
  @Autowired
  private TeleTanClientSI teleTanClient;

  /**
   * The Web GUI page request showing the index.html web page without a teletan
   *
   * @param request the http request object
   * @param model the thymeleaf model
   * @return the name of the Thymeleaf template to be used for the HTML page
   */
  @GetMapping(ROUTE_INDEX)
  public String index(HttpServletRequest request, Model model) {
    KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken)request.getUserPrincipal();
    model.addAttribute(ATTR_USER, ((KeycloakPrincipal)principal.getPrincipal()).getName());

    HttpSession session = request.getSession();
    if (session != null) {
      session.setAttribute(SESSION_ATTR_TELETAN, "TeleTAN");
    }
    return TEMPLATE_INDEX;
  }

  /**
   * The Web GUI page request showing the index.html or teletan.html web page
   * The index.html is shown when the session was newly create (directly after login)
   * other wise the teletan page with
   *
   * @param request the http request object
   * @param model the thymeleaf model
   * @return the name of the Thymeleaf template to be used for the HTML page
   */
  @GetMapping(ROUTE_TELETAN)
  public String home(HttpServletRequest request, Model model) {

    TeleTan teleTan = new TeleTan("123456789");
    KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken)request.getUserPrincipal();

    String template = TEMPLATE_INDEX;
    HttpSession session = request.getSession();
    if (session != null) {
      if (session.getAttribute(SESSION_ATTR_TELETAN) != null) {
        // get a new teleTAN and switch to the TEMPLATE_TELETAN
        teleTan = teleTanClient.createTeleTan();
        template = TEMPLATE_TELETAN;
      }
      session.setAttribute(SESSION_ATTR_TELETAN, "TeleTAN");
    }

    if (model == null) {
      //TODO fix by proper implementation of unit test
      return teleTan.getValue();
    } else {
      // set thymeleaf attributes (teleTAN and user name)
      model.addAttribute(ATTR_TELETAN, teleTan.getValue());
      model.addAttribute(ATTR_USER, ((KeycloakPrincipal)principal.getPrincipal()).getName());
    }

    return template;
  }
  
  /**
   * The Get request to log out from the portal web site
   *
   * @param request the http request object
   * @return the redirect path after the logout
   */
  @GetMapping(ROUTE_LOGOUT)
  public String logout(HttpServletRequest request) {
    try {
      request.logout();
    } catch (ServletException e) {
      log.error("Logout failed", e);
    }
    return "redirect:" + TEMPLATE_TELETAN;
  }
}
