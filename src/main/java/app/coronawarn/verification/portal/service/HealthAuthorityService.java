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

package app.coronawarn.verification.portal.service;

import app.coronawarn.verification.portal.config.VerificationPortalConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthAuthorityService {

  private final VerificationPortalConfigurationProperties configurationProperties;

  private final ObjectMapper objectMapper;

  private List<HealthAuthority> healthAuthorities = new ArrayList<>();

  @PostConstruct
  private void loadData() throws JsonProcessingException {
    healthAuthorities = objectMapper.readValue(
      configurationProperties.getHealthAuthoritiesList(),
      objectMapper.getTypeFactory().constructCollectionType(List.class, HealthAuthority.class));
  }

  /**
   * Method to check whether a given Health-Authority ID is valid.
   *
   * @param haid ID (Nr) of the Health Authority
   * @return The corresponding name or null if HAID is invalid.
   */
  public String checkHealthAuthority(String haid) {
    return healthAuthorities.stream()
      .filter(healthAuthority -> healthAuthority.nr.equals(haid))
      .findFirst()
      .map(HealthAuthority::getName)
      .orElse(null);
  }

  @Getter
  @Setter
  public static class HealthAuthority {

    private String name;

    private String nr;
  }


}
