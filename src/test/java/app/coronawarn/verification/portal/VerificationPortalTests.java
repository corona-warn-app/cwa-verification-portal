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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * This is the test class for the verification portal application.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(properties = {"log4j.configurationFile=log4j2-test.xml"})
@TestPropertySource("classpath:test.properties")
public class VerificationPortalTests {

    static final Logger LOG = LogManager.getLogger();

    @Autowired
    private MockMvc mockMvc;

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
    public void callGenerateTAN() throws Exception {
        LOG.info("VerficationAppTests callGenerateTAN()");

        prepareAppSessionTestData();

    }

    private void prepareAppSessionTestData() {
    }
 }
