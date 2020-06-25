package app.coronawarn.verification.portal.service;

import app.coronawarn.verification.portal.client.TeleTan;
import app.coronawarn.verification.portal.client.VerificationServerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TeleTanService {

  @Autowired
  private VerificationServerClient verificationServerClient;
  
  public static final String TOKEN_PREFIX = "Bearer ";

  public TeleTan createTeleTan(String token) {
    return verificationServerClient.createTeleTan(TOKEN_PREFIX + token);
  }

}
