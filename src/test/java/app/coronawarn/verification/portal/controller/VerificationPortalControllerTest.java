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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import app.coronawarn.verification.portal.client.TeleTan;
import app.coronawarn.verification.portal.config.VerificationPortalConfigurationProperties;
import app.coronawarn.verification.portal.service.HealthAuthorityService;
import app.coronawarn.verification.portal.service.TeleTanService;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.ServletUnitTestingSupport;
import feign.FeignException;
import feign.Request;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;


@Slf4j
@WebMvcTest(VerificationPortalController.class)
@TestPropertySource(properties = {
  "rateLimiting.enabled=true",
  "rateLimiting.seconds=30",
  "cwa.verification-portal.health-authorities-list=[{\"name\": \"Demo HA\", \"nr\": \"1337\"}]"})
@ContextConfiguration(classes = VerificationPortalController.class)
@Import({VerificationPortalConfigurationProperties.class, HealthAuthorityService.class})
public class VerificationPortalControllerTest extends ServletUnitTestingSupport {

  public static final String TELETAN_NAME = "teletan";
  public static final String TELETAN_VALUE = "TeleTAN";
  private static final String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";

  private HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;
  private CsrfToken csrfToken;

  @MockBean
  TeleTanService teleTanService;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
  }

  /**
   * Test of index method, of class VerificationPortalController.
   *
   * @throws Exception if the test cannot be performed.
   */
  @Test
  @WithMockKeycloakAuth("Role_Test")
  public void testIndex() throws Exception {
    log.info("process testIndex()");
    mockMvc.perform(get("/cwa"))
      .andExpect(status().isOk())
      .andExpect(view().name("index"));
    mockMvc.perform(get("/"))
      .andExpect(status().isOk())
      .andExpect(view().name("index"));
  }

  /**
   * Test of start method, of class VerificationPortalController.
   *
   * @throws Exception if the test cannot be performed.
   */
  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester1"), authorities = {"ROLE_c19hotline", "ROLE_c19hotline_event"})
  public void testStart() throws Exception {
    log.info("process testStart() RequestMethod.GET");
    mockMvc.perform(get("/cwa/start"))
      .andExpect(status().isOk())
      .andExpect(view().name("start"))
      .andExpect(model().attribute("userName", equalTo("tester1")))
      .andExpect(model().attribute("role_test", equalTo(true)))
      .andExpect(model().attribute("role_event", equalTo(true)))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";

    log.info("process testStart() RequestMethod.POST");
    mockMvc.perform(post("/cwa/start")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE))
      .andExpect(status().isOk())
      .andExpect(view().name("start"))
      .andExpect(model().attribute("userName", equalTo("tester1")))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));
  }

  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester2"), authorities = {"ROLE_c19hotline"})
  public void testStartOnlyTestRole() throws Exception {
    log.info("process testStartOnlyTestRole()");
    mockMvc.perform(get("/cwa/start"))
      .andExpect(status().isOk())
      .andExpect(view().name("start"))
      .andExpect(model().attribute("userName", equalTo("tester2")))
      .andExpect(model().attribute("role_test", equalTo(true)))
      .andExpect(model().attribute("role_event", equalTo(false)))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));
  }

  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester3"), authorities = {"ROLE_c19hotline_event"})
  public void testStartOnlyEventRole() throws Exception {
    log.info("process testStartOnlyEventRole()");
    mockMvc.perform(get("/cwa/start"))
      .andExpect(status().isOk())
      .andExpect(view().name("start"))
      .andExpect(model().attribute("userName", equalTo("tester3")))
      .andExpect(model().attribute("role_test", equalTo(false)))
      .andExpect(model().attribute("role_event", equalTo(true)))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));
  }

  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester4"), authorities = {})
  public void testStartNoRole() throws Exception {
    log.info("process testStartNoRole()");
    mockMvc.perform(get("/cwa/start"))
      .andExpect(status().isOk())
      .andExpect(view().name("start"))
      .andExpect(model().attribute("userName", equalTo("tester4")))
      .andExpect(model().attribute("role_test", equalTo(false)))
      .andExpect(model().attribute("role_event", equalTo(false)))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));
  }

  /**
   * Test of start method, of class VerificationPortalController.
   *
   * @throws Exception if the test cannot be performed.
   */
  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester5"), value = "Role_Test")
  public void testStartNotFound() throws Exception {
    log.info("process testStartNotFound()");
    mockMvc.perform(get("/corona/start"))
      .andExpect(status().isNotFound());
  }

  /**
   * Test of teletan method, of class VerificationPortalController.
   *
   * @throws Exception if the test cannot be performed.
   */
  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester6.1"), authorities = {"ROLE_c19hotline", "ROLE_c19hotline_event"})
  public void testTeletanEvent() throws Exception {
    log.info("process testTeletanEvent()");

    when(teleTanService.createTeleTan(any(String.class), eq("EVENT"))).thenReturn(new TeleTan("123454321"));

    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE)
        .param("HAID", "1337")
        .param("EVENT", "Event Button Clicked")
        .param("TEST", ""))
      .andExpect(status().isOk())
      .andExpect(view().name(TELETAN_NAME))
      .andExpect(model().attribute("userName", equalTo("tester6.1")))
      .andExpect(model().attribute("teleTAN", equalTo("123454321")))
      .andExpect(model().attribute("role_test", equalTo(true)))
      .andExpect(model().attribute("role_event", equalTo(true)))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));

    // check rate limiting
    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE))
      .andExpect(status().isTooManyRequests());
  }

  /**
   * Test of teletan method, of class VerificationPortalController.
   *
   * @throws Exception if the test cannot be performed.
   */
  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester6"), authorities = {"ROLE_c19hotline", "ROLE_c19hotline_event"})
  public void testTeletanEvent_InvalidHaId() throws Exception {
    log.info("process testTeletanEvent()");

    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE)
        .param("HAID", "1338")
        .param("EVENT", "Event Button Clicked")
        .param("TEST", ""))
      .andExpect(status().isBadRequest());

    verify(teleTanService, never()).createTeleTan(any(String.class), eq("EVENT"));
  }

  /**
   * Test of teletan method, of class VerificationPortalController.
   *
   * @throws Exception if the test cannot be performed.
   */
  @Test
  @WithMockKeycloakAuth(authorities = {"ROLE_c19hotline", "ROLE_c19hotline_event"})
  public void testTeletanEvent_MissingHaId() throws Exception {
    log.info("process testTeletanEvent()");

    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE)
        .param("EVENT", "Event Button Clicked")
        .param("TEST", ""))
      .andExpect(status().isBadRequest());

    verify(teleTanService, never()).createTeleTan(any(String.class), eq("EVENT"));
  }

  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester7"), authorities = {"ROLE_c19hotline", "ROLE_c19hotline_event"})
  public void testTeletanTest() throws Exception {
    log.info("process testTeletanTest()");

    when(teleTanService.createTeleTan(any(String.class), eq("TEST"))).thenReturn(new TeleTan("123454321"));

    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE)
        .param("EVENT", "")
        .param("TEST", "TEST Button clicked"))
      .andExpect(status().isOk())
      .andExpect(view().name(TELETAN_NAME))
      .andExpect(model().attribute("userName", equalTo("tester7")))
      .andExpect(model().attribute("teleTAN", equalTo("123454321")))
      .andExpect(model().attribute("role_test", equalTo(true)))
      .andExpect(model().attribute("role_event", equalTo(true)))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));

    // check rate limiting
    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE))
      .andExpect(status().isTooManyRequests());
  }

  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester8"), authorities = {"ROLE_c19hotline"})
  public void testRoleMappingOnlyHotline() throws Exception {
    log.info("process testRoleMappingOnlyHotline()");

    when(teleTanService.createTeleTan(any(String.class), eq("TEST"))).thenReturn(new TeleTan("123454321"));

    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE)
        .param("EVENT", "")
        .param("TEST", "TEST Button clicked"))
      .andExpect(status().isOk())
      .andExpect(view().name(TELETAN_NAME))
      .andExpect(model().attribute("userName", equalTo("tester8")))
      .andExpect(model().attribute("teleTAN", equalTo("123454321")))
      .andExpect(model().attribute("role_test", equalTo(true)))
      .andExpect(model().attribute("role_event", equalTo(false)))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));

    // check rate limiting
    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE))
      .andExpect(status().isTooManyRequests());
  }

  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester9"), authorities = {"ROLE_c19hotline_event"})
  public void testRoleMappingOnlyEvent() throws Exception {
    log.info("process testRoleMappingOnlyEvent()");

    when(teleTanService.createTeleTan(any(String.class), eq("TEST"))).thenReturn(new TeleTan("123454321"));

    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE)
        .param("EVENT", "")
        .param("TEST", "TEST Button clicked"))
      .andExpect(status().isOk())
      .andExpect(view().name(TELETAN_NAME))
      .andExpect(model().attribute("userName", equalTo("tester9")))
      .andExpect(model().attribute("teleTAN", equalTo("123454321")))
      .andExpect(model().attribute("role_test", equalTo(false)))
      .andExpect(model().attribute("role_event", equalTo(true)))
      .andExpect(request().sessionAttribute(TELETAN_NAME, equalTo(TELETAN_VALUE)));

    // check rate limiting
    mockMvc.perform(post("/cwa/teletan")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE))
      .andExpect(status().isTooManyRequests());
  }

  /**
   * Test of logout method, of class VerificationPortalController.
   *
   * @throws Exception if the test cannot be performed.
   */
  @Test
  @WithMockKeycloakAuth("Role_Test")
  public void testLogout() throws Exception {
    log.info("process testLogout()");

    mockMvc.perform(post("/cwa/logout")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
      .andExpect(redirectedUrl("start"))
      .andExpect(status().isFound());
  }

  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester10"), value = "ROLE_c19hotline")
  public void testIfRateLimitExceptionIsHandledCorrectly() throws Exception {
    Request dummyRequest = Request.create(Request.HttpMethod.GET, "url", Collections.emptyMap(), null, null, null);
    Mockito.doThrow(new FeignException.TooManyRequests("", dummyRequest, null, Collections.emptyMap())).when(teleTanService).createTeleTan(any(String.class), any(String.class));

    mockMvc.perform(post("/cwa/teletan")
        .param("EVENT", "")
        .param("TEST", "TEST Button clicked")
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE))
      .andExpect(status().isTooManyRequests());
  }

  @Test
  @WithMockKeycloakAuth(claims = @OpenIdClaims(preferredUsername = "tester11"), value = "Role_Test")
  public void testIfAnyOtherExceptionIsJustForwared() {
    given(teleTanService.createTeleTan(any(String.class), any(String.class))).willAnswer(invocation -> {
      throw new Exception("Dummy Exception");
    });
    Assertions.assertThrows(Exception.class, () -> mockMvc.perform(post("/cwa/teletan")
      .param("EVENT", "")
      .param("TEST", "TEST Button clicked")
      .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
      .sessionAttr(TELETAN_NAME, TELETAN_VALUE).param(TELETAN_NAME, TELETAN_VALUE)));
  }

}
