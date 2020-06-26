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

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * The Spring Boot application class.
 */
@SpringBootApplication
@EnableFeignClients
public class VerificationPortalApplication {

  public static void main(String[] args) {
    SpringApplication.run(VerificationPortalApplication.class, args);
  }

  /**
   * Enable the cipher suites from server to be preferred.
   *
   * @return the WebServerFactoryCustomizer with cipher suites configuration
   */
  @Bean
  @ConditionalOnProperty(value = "server.ssl.cipher.suites.order", havingValue = "true")
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer() {
    return factory -> factory
      .addConnectorCustomizers(connector ->
        ((AbstractHttp11Protocol<?>) connector.getProtocolHandler())
          .setUseServerCipherSuitesOrder(true)
      );
  }

}
