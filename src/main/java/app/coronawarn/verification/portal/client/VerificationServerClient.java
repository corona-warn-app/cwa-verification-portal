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

package app.coronawarn.verification.portal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cwa-verification-server",
  url = "${cwa-verification-server.url}",
  configuration = VerificationServerClientConfig.class)
public interface VerificationServerClient {
  
  public static final String HEADER_NAME_AUTHORIZATION  = "Authorization";

  /**
   * Call the verification service to get teletan from token.
   *
   * @param token the token to request teletan
   * @return the teletan
   */
  @PostMapping(value = "/version/v1/tan/teletan",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  TeleTan createTeleTan(@RequestHeader(HEADER_NAME_AUTHORIZATION) String token);

}
