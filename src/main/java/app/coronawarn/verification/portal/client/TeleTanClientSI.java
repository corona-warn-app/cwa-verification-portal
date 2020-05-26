package app.coronawarn.verification.portal.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface TeleTanClientSI {

  @RequestMapping(value = "${cwa-verification-server.path.teletan}", method = RequestMethod.POST, produces = {
    MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
  TeleTan createTeleTan();

}
