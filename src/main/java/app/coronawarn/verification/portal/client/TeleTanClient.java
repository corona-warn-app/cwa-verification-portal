package app.coronawarn.verification.portal.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("teleTanClient")
public class TeleTanClient implements TeleTanClientSI {

  @Autowired
  private VerificationServerFeignClient feignClient;

  @Override
  public TeleTan createTeleTan() {
    return feignClient.createTeleTan();
  }

}
