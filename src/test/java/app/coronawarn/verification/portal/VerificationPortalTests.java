/*
 * Corona-Warn-App / cwa-verification
 *
 * (C) 2020, T-Systems International GmbH
 *
 * Deutsche Telekom AG, SAP AG and all other contributors /
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.coronawarn.verification.portal.controller.VerificationPortalController;

/**
 * This is the test class for the verification portal application.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(properties = {"log4j.configurationFile=log4j2-test.xml"})
@TestPropertySource("classpath:test.yml")
public class VerificationPortalTests {


  private static final Logger LOG = LogManager.getLogger();

  private static final String ROUTE_TELETAN = "/teletan";


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private VerificationPortalController verificationPortalController;

//    @MockBean
//    private VerificationAppSessionService appSessionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test generateTAN.
   *
   * @throws Exception if the test cannot be performed.
   */
  @Test
  public void createTeletanTest() throws Exception {
    LOG.info("VerficationAppTests callGenerateTAN()");
    prepareAppSessionTestData();

    //mockMvc.perform(get(ROUTE_TELETAN))
    //  .andExpect(status().isCreated());
    String home = verificationPortalController.home(null);
    Assert.assertEquals("1abc56N", home);
  }

  private void prepareAppSessionTestData() {
  }
}
