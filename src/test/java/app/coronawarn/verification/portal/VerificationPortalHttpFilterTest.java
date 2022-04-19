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

package app.coronawarn.verification.portal;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = VerificationPortalHttpFilter.class)
@TestPropertySource(properties = {"host-header.whitelist=localhost,localhost:8081", "pod.ip=127.0.0.1", "pod.port=8081"})
@EnableConfigurationProperties
public class VerificationPortalHttpFilterTest {

  private static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
  private static final String INVALID_HOST = "invalid-server.local";
  private static final String INVALID_PORT = "9988";

  private static final String VALID_HOST = "localhost";
  private static final String VALID_HOST_PORT = "localhost:8081";

  private static final String POD_HOST = "127.0.0.1";
  private static final String POD_PORT = "8081";
  private static final String INVALID_POD_PORT = "8085";

  @Autowired
  private VerificationPortalHttpFilter verificationPortalHttpFilter;

  @Test
  public void doFilterReturnsOkForValidHost() throws IOException, ServletException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext());
    request.addHeader(HttpHeaders.HOST, VALID_HOST);
    verificationPortalHttpFilter.doFilter(request, response, new MockFilterChain());
    assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }

  @Test
  public void doFilterReturnsOkForValidHostAndPort() throws IOException, ServletException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext());
    request.addHeader(HttpHeaders.HOST, VALID_HOST_PORT);
    verificationPortalHttpFilter.doFilter(request, response, new MockFilterChain());
    assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }

  @Test
  public void doFilterReturnsOkForValidPodIPAndHost() throws IOException, ServletException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext());
    request.addHeader(HttpHeaders.HOST, POD_HOST + ":" + POD_PORT);
    verificationPortalHttpFilter.doFilter(request, response, new MockFilterChain());
    assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }

  @Test
  public void doFilterReturnsBadRequestForValidPodPort() throws IOException, ServletException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext());
    request.addHeader(HttpHeaders.HOST, POD_HOST + ":" + INVALID_POD_PORT);
    verificationPortalHttpFilter.doFilter(request, response, new MockFilterChain());
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

  @Test
  public void doFilterReturnsBadRequestWhenXForwardedHostHeaderInRequest() throws IOException, ServletException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext());
    request.addHeader(HttpHeaders.HOST, VALID_HOST);
    request.addHeader(X_FORWARDED_HOST_HEADER, INVALID_HOST);
    verificationPortalHttpFilter.doFilter(request, response, new MockFilterChain());
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

  @Test
  public void doFilterReturnsBadRequestWhenHostHeaderNotInRequest() throws IOException, ServletException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext());
    verificationPortalHttpFilter.doFilter(request, response, new MockFilterChain());
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

  @Test
  public void doFilterReturnsBadRequestWhenHostHeaderIsNotValid() throws IOException, ServletException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext());
    request.addHeader(HttpHeaders.HOST, INVALID_HOST);
    verificationPortalHttpFilter.doFilter(request, response, new MockFilterChain());
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

  @Test
  public void doFilterReturnsBadRequestWhenPortInHostHeaderNotValid() throws IOException, ServletException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext());
    request.addHeader(HttpHeaders.HOST, VALID_HOST + ":" + INVALID_PORT);
    verificationPortalHttpFilter.doFilter(request, response, new MockFilterChain());
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

}
