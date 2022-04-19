/*
 * Corona-Warn-App / cwa-verification
 *
 * (C) 2020 - 2022, T-Systems International GmbH
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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class represents the server side rate limitation Exception.
 */
@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS, reason = "ServerRateLimit")
public class ServerRateLimitationException extends RateLimitationException {

  public ServerRateLimitationException() {
    super();
  }

  public ServerRateLimitationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServerRateLimitationException(String message) {
    super(message);
  }

  public ServerRateLimitationException(Throwable cause) {
    super(cause);
  }

}
