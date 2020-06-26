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
import app.coronawarn.verification.portal.service.TeleTanService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class represents the WEB UI controller for the verification portal. It implements a very
 * simple HTML interface with one submit button to get and show a newly generated TeleTAN.
 */
@Slf4j
@Controller
public class VerificationPortalController {

  /**
   * The route to the TeleTAN portal teletan web site.
   */
  public static final String ROUTE_TELETAN = "/cwa/teletan";
  /**
   * Session attribute showing that the index template has been shown already at least once in the
   * current session (means that now the teletan template should be shown instead of the index
   * template).
   */
  static final String SESSION_ATTR_TELETAN = "teletan";
  /**
   * The route(s) to the TeleTAN portal start web site.
   */
  private static final String ROUTE_INDEX = "/";
  private static final String ROUTE_CWA = "/cwa";
  private static final String ROUTE_START = "/cwa/start";
  /**
   * The route to log out from the portal web site.
   */
  private static final String ROUTE_LOGOUT = "/cwa/logout";

  /**
   * The html Thymeleaf template for the TeleTAN portal web site.
   */
  private static final String TEMPLATE_TELETAN = "teletan";

  /**
   * The html Thymeleaf template for the TeleTAN portal start web site.
   */
  private static final String TEMPLATE_START = "start";

  /**
   * The html Thymeleaf template for the TeleTAN portal index site.
   */
  private static final String TEMPLATE_INDEX = "index";

  /**
   * The Thymeleaf attributes used for displaying the teletan and the current user.
   */
  private static final String ATTR_TELETAN = "teleTAN";
  private static final String ATTR_USER = "userName";
  private static final String ATTR_PW_RESET_URL = "pwResetUrl";

  /**
   * The Keycloak password reset URL.
   */
  @Value("${keycloak-pw.reset-url}")
  private String pwResetUrl;
  
  private static final Map<String, LocalDateTime> rateLimitingUserMap = new ConcurrentHashMap<String, LocalDateTime>();
  
  @Value("${rateLimiting.enabled}")
  private boolean rateLimitingEnabled;

  @Value("${rateLimiting.seconds}")
  private long rateLimitingSeconds;

  /**
   * The REST client interface for getting the TeleTAN from verificationserver.
   */
  private final TeleTanService teleTanService;

  public VerificationPortalController(TeleTanService teleTanService) {
    this.teleTanService = teleTanService;
  }

  /**
   * The Web GUI page request showing the index.html web page
   *
   * @return the name of the Thymeleaf template to be used for the HTML page
   */
  @GetMapping({ROUTE_INDEX, ROUTE_CWA})
  public String index() {
    return TEMPLATE_INDEX;
  }

  /**
   * The Web GUI page request showing the start.html web page without a teleTan.
   *
   * @param request the http request object
   * @param model   the thymeleaf model
   * @return the name of the Thymeleaf template to be used for the HTML page
   */
  @RequestMapping(value = ROUTE_START, method = {RequestMethod.GET, RequestMethod.POST})
  public String start(HttpServletRequest request, Model model) {
    KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request
      .getUserPrincipal();
    String user = ((KeycloakPrincipal) principal.getPrincipal()).getName();

    if (model != null) {
      model.addAttribute(ATTR_USER, user.replace("<", "").replace(">", ""));
      model.addAttribute(ATTR_PW_RESET_URL, pwResetUrl);
    }

    HttpSession session = request.getSession();
    if (session != null) {
      session.setAttribute(SESSION_ATTR_TELETAN, "TeleTAN");
    }
    return TEMPLATE_START;
  }

  /**
   * The Web GUI page request showing the start.html or teletan.html web page The start.html is
   * shown when the session was newly created (directly after login) otherwise the teletan page with
   * retrieved teleTan is to be displayed.
   *
   * @param request the http request object
   * @param model   the thymeleaf model
   * @return the name of the Thymeleaf template to be used for the HTML page
   */
  @PostMapping(value = ROUTE_TELETAN)
  public String teletan(HttpServletRequest request, Model model) {
    TeleTan teleTan = new TeleTan("123456789");
    KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request
      .getUserPrincipal();
    String user = ((KeycloakPrincipal) principal.getPrincipal()).getName();

    // initially the TEMPLATE_INDEX is used (without showing the teleTAN)
    String template = TEMPLATE_START;
    HttpSession session = request.getSession();
    if (session != null) {
      if (session.getAttribute(SESSION_ATTR_TELETAN) != null) {
        // get a new teleTan and switch to the TEMPLATE_TELETAN
        String token = principal.getAccount().getKeycloakSecurityContext()
          .getTokenString();
        if (rateLimitingEnabled) {
          checkRateLimitation(user);
        } 
        teleTan = teleTanService.createTeleTan(token);
        log.info("TeleTan successfully retrieved for user: {}", user);
        template = TEMPLATE_TELETAN;
      }
      session.setAttribute(SESSION_ATTR_TELETAN, "TeleTAN");
    }
    if (model != null) {
      model.addAttribute(ATTR_TELETAN, teleTan.getValue().replace("<", "").replace(">", ""));
      model.addAttribute(ATTR_USER, user.replace("<", "").replace(">", ""));
      model.addAttribute(ATTR_PW_RESET_URL, pwResetUrl);
    }
    return template;
  }

  private void checkRateLimitation(String user) {
    LocalDateTime usageTime = rateLimitingUserMap.get(user);
    if (usageTime != null) {
      if (LocalDateTime.now().minusSeconds(rateLimitingSeconds).isBefore(usageTime)) {
        throw new RateLimitationException("Too many requests by user: " + user + " in a given amount of time");
      } else {
        rateLimitingUserMap.replace(user, LocalDateTime.now());
      }
    } else {
      rateLimitingUserMap.put(user, LocalDateTime.now());
    }
  }

  /**
   * The Get request to log out from the portal web site.
   *
   * @param request the http request object
   * @return the redirect path after the logout
   */
  @PostMapping(ROUTE_LOGOUT)
  public String logout(HttpServletRequest request) {
    try {
      request.logout();
    } catch (ServletException e) {
      log.error("Logout failed", e);
    }
    return "redirect:" + TEMPLATE_START;
  }
}
