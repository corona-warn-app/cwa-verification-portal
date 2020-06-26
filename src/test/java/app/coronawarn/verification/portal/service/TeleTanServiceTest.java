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

package app.coronawarn.verification.portal.service;

import app.coronawarn.verification.portal.client.TeleTan;
import app.coronawarn.verification.portal.client.VerificationServerClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TeleTanServiceTest
{
  public static final String TEST_TELE_TAN = "FE9A5MAK6C";
  public static final String TEST_TOKEN = "0815";
  
  @Autowired
  @InjectMocks
  private TeleTanService teleTanService;

  @Mock
  private VerificationServerClient clientMock;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }  
  
  /**
   * Test of createTeleTan method, of class TeleTanService.
   */
  @Test
  public void testCreateTeleTan() {
    log.info("process testCreateTeleTan()");
    Mockito.doReturn(new TeleTan(TEST_TELE_TAN)).when(clientMock).createTeleTan(TeleTanService.TOKEN_PREFIX + TEST_TOKEN);
    assertThat(teleTanService.createTeleTan(TEST_TOKEN).equals(new TeleTan(TEST_TELE_TAN)));
  }
}
