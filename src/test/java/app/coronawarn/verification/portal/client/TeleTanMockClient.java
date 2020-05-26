package app.coronawarn.verification.portal.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Primary
@Slf4j
@Qualifier("teleTanClient")
public class TeleTanMockClient implements TeleTanClientSI {

  @Override
  public TeleTan createTeleTan() {
    log.debug("Calling TeleTanMockClient - onSettingChanged");
    return new TeleTan("1abc56N");
  }

}
