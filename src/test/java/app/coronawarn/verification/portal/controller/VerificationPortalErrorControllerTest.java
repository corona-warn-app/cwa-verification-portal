/*
 * Corona-Warn-App / cwa-verification-portal
 *
 * (C) 2020 - 2022, T-Systems International GmbH
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;
import jakarta.servlet.RequestDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@AutoConfigureMockMvc
@WebMvcTest(VerificationPortalController.class)
@TestPropertySource(properties = {"rateLimiting.enabled=true", "rateLimiting.seconds=30"})
@ContextConfiguration(classes = VerificationPortalErrorController.class)
public class VerificationPortalErrorControllerTest {
  private static final String ATTR_ERROR_MSG = "message";
  private static final String SERVER_RATE_LIMIT_ERROR_REASON = "ServerRateLimit";

  private static final String EXPECTED_ERROR_404_MESSAGE = "Die aufgerufene Seite konnte nicht gefunden werden.";
  private static final String EXPECTED_ERROR_403_MESSAGE = "Der Benutzer kann nicht authentifiziert werden.";
  private static final String EXPECTED_ERROR_429_MESSAGE = "Die Zeitlimitierung für TeleTAN Anfragen ist aktiv, bitte warten Sie ";
  private static final String EXPECTED_SECONDS_MESSAGE = " Sekunden.";
  private static final String EXPECTED_RATE_LIMIT_SERVER_TEXT_MESSAGE = "einen Moment.";
  private static final String EXPECTED_ERROR_MESSAGE = "Es kann keine TeleTAN aufgrund eines internen Fehlers generiert werden.";

  @Value("${rateLimiting.seconds}")
  private long rateLimitingSeconds;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockJwtAuth(claims = @OpenIdClaims(sub = "tester"), value = "Role_Test")
  public void handleErrorHandlesNotFoundCorrectly() throws Exception {
    log.info("process handleErrorHandlesNotFoundCorrectly() RequestMethod.POST");
    mockMvc.perform(post("/error")
        .with(csrf().asHeader())
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value()))
      .andExpect(status().isOk())
      .andExpect(view().name("error"))
      .andExpect(model().attribute(ATTR_ERROR_MSG, equalTo(EXPECTED_ERROR_404_MESSAGE)));
  }

  @Test
  @WithMockJwtAuth(claims = @OpenIdClaims(sub = "tester"), value = "Role_Test")
  public void handleErrorHandlesForbiddenCorrectly() throws Exception {
    log.info("process handleErrorHandlesForbiddenCorrectly() RequestMethod.POST");
    mockMvc.perform(post("/error")
        .with(csrf().asHeader())
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.FORBIDDEN.value()))
      .andExpect(status().isOk())
      .andExpect(view().name("error"))
      .andExpect(model().attribute(ATTR_ERROR_MSG, equalTo(EXPECTED_ERROR_403_MESSAGE)));
  }

  @Test
  @WithMockJwtAuth(claims = @OpenIdClaims(sub = "tester"), value = "Role_Test")
  public void handleErrorHandlesTooManyRequestsWithRateLimitCorrectly() throws Exception {
    log.info("process handleErrorHandlesTooManyRequestsWithRateLimitCorrectly() RequestMethod.POST");
    mockMvc.perform(post("/error")
        .with(csrf().asHeader())
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.TOO_MANY_REQUESTS.value())
        .requestAttr(RequestDispatcher.ERROR_MESSAGE, SERVER_RATE_LIMIT_ERROR_REASON))
      .andExpect(status().isOk())
      .andExpect(view().name("error"))
      .andExpect(model().attribute(ATTR_ERROR_MSG, equalTo(EXPECTED_ERROR_429_MESSAGE + EXPECTED_RATE_LIMIT_SERVER_TEXT_MESSAGE)));
  }

  @Test
  @WithMockJwtAuth(claims = @OpenIdClaims(sub = "tester"), value = "Role_Test")
  public void handleErrorHandlesTooManyRequestsCorrectly() throws Exception {
    log.info("process handleErrorHandlesTooManyRequestsCorrectly() RequestMethod.POST");
    mockMvc.perform(post("/error")
        .with(csrf().asHeader())
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.TOO_MANY_REQUESTS.value())
        .requestAttr(RequestDispatcher.ERROR_MESSAGE, ""))
      .andExpect(status().isOk())
      .andExpect(view().name("error"))
      .andExpect(model().attribute(ATTR_ERROR_MSG, equalTo(EXPECTED_ERROR_429_MESSAGE + rateLimitingSeconds + EXPECTED_SECONDS_MESSAGE)));
  }

  @Test
  @WithMockJwtAuth(claims = @OpenIdClaims(sub = "tester"), value = "Role_Test")
  public void handleErrorHandlesDefaultCorrectly() throws Exception {
    log.info("process handleErrorHandlesDefaultCorrectly() RequestMethod.POST");
    mockMvc.perform(post("/error")
        .with(csrf().asHeader())
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.I_AM_A_TEAPOT.value()))
      .andExpect(status().isOk())
      .andExpect(view().name("error"))
      .andExpect(model().attribute(ATTR_ERROR_MSG, equalTo(EXPECTED_ERROR_MESSAGE)));
  }

}
