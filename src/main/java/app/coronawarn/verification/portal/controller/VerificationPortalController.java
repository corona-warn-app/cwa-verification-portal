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

package app.coronawarn.verification.portal.controller;


import app.coronawarn.verification.portal.client.TeleTanClientSI;
import app.coronawarn.verification.portal.client.TeleTan;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Random;
import java.util.UUID;


/**
 * This class represents the WEB UI controller for the verification portal.
 * It implements a very simple HTML interface with one submit button to get and show a newly generated TeleTAN
 */
@Controller
public class VerificationPortalController {

  /**
   * The route to the TeleTAN portal web site.
   */
  private static final String ROUTE_TELETAN = "/teletan";

  /**
   * The route to the TeleTAN portal web site
   */
  private static final String ROUTE_INDEX = "/";

  /**
   * The html Thymeleaf template for the TeleTAN portal web site
   */
  private static final String TEMPLATE_TELETAN = "teletan";

  /**
   * The html Thymeleaf template for the TeleTAN portal web site
   */
  private static final String TEMPLATE_INDEX = "index";

  /**
   * The Thymeleaf attribute used for displaying the teletan
   */
  private static final String ATTR_TELETAN = "teleTAN";


  /**
   * The logger.
   */
  private static final Logger LOG = LogManager.getLogger();

  /**
   * The REST client interface for getting the TeleTAN from verificationserver.
   */
  @Autowired
  private TeleTanClientSI teleTanClient;


  /**
   * The Web GUI page request showing the index.html web page without a teletan
   *
   * @param model the thymeleaf model
   * @return the name of the HTML Thymeleaf template to be used for the HTML page
   */
  @GetMapping(ROUTE_INDEX)
  public String index(Model model) {
    return TEMPLATE_INDEX;
  }

  /**
   * The Web GUI page request showing the teletan.html web page with a newly created TeleTAN
   *
   * @param model the thymeleaf model
   * @return the name of the HTML Thymeleaf template to be used for the HTML page
   */
  @GetMapping(ROUTE_TELETAN)
  public String home(Model model) {
    // try to get the teleTan from the verification server
    TeleTan teleTan = teleTanClient.createTeleTan();

    //TODO generate dummy TeleTAN until the TeleTAN service will be available (or stubbed)
    //String teleTan = String.valueOf(Math.abs(new Random().nextInt()));
    if (model == null) {
      //TODO fix by proper implementation of unit test
      return teleTan.getValue();
    } else {
      model.addAttribute(ATTR_TELETAN, teleTan.getValue());
    }
    return TEMPLATE_TELETAN;
  }

}
