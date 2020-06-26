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

package app.coronawarn.verification.portal;

import app.coronawarn.verification.portal.controller.VerificationPortalController;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;


@Slf4j
@EnableSpringHttpSession
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

  private static final String ROLE_C19HOTLINE = "c19hotline";
  private static final String ACTUATOR_ROUTE = "/actuator/**";

  private static final String SAMESITE_LAX = "Lax";
  private static final String SET_COOKIE_HEADER = "Set-Cookie";
  private static final String COOKIE_HEADER = "Cookie";
  private static final String OAUTH_TOKEN_REQUEST_STATE_COOKIE = "OAuth_Token_Request_State";
  private static final String SESSION_COOKIE = "SESSION";

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
    auth.authenticationProvider(keycloakAuthenticationProvider);
  }

  @Bean
  public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http
      .headers().addHeaderWriter(this::modifyResponseSetCookieHeader)
      .and()
      .authorizeRequests()
      .mvcMatchers(HttpMethod.GET, ACTUATOR_ROUTE).permitAll()
      .antMatchers(VerificationPortalController.ROUTE_TELETAN)
      .hasRole(ROLE_C19HOTLINE)
      .anyRequest().authenticated();
  }

  @Bean
  public CookieSerializer defaultCookieSerializer() {
    DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
    cookieSerializer.setCookieName(SESSION_COOKIE);
    cookieSerializer.setSameSite(SAMESITE_LAX);
    cookieSerializer.setUseHttpOnlyCookie(true);
    return cookieSerializer;
  }

  @Bean
  public SessionRepository sessionRepository() {
    return new MapSessionRepository(new ConcurrentHashMap<>());
  }

  private void modifyResponseSetCookieHeader(final HttpServletRequest request, final HttpServletResponse response) {
    final Collection<String> setCookieValues = response.getHeaders(SET_COOKIE_HEADER);
    for (String setCookie : setCookieValues) {
      if (setCookie.contains(OAUTH_TOKEN_REQUEST_STATE_COOKIE) && requestContainsSessionCookie(request)) {
        response.setHeader(SET_COOKIE_HEADER, addSameSiteStrict(setCookie));
      } else {
        log.warn("Request does not contain session cookie");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    }
  }

  private String addSameSiteStrict(String setCookie) {
    return setCookie + "; SameSite=" + SAMESITE_LAX;
  }

  private boolean requestContainsSessionCookie(final HttpServletRequest request) {
    final String cookie = request.getHeader(COOKIE_HEADER);
    if (cookie == null) {
      return false;
    }
    return cookie.contains(SESSION_COOKIE);
  }

}
