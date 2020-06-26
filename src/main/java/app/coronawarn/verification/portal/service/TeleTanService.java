package app.coronawarn.verification.portal.service;

import app.coronawarn.verification.portal.client.TeleTan;
import app.coronawarn.verification.portal.client.VerificationServerClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeleTanService {
  
  @NonNull
  private VerificationServerClient verificationServerClient;
 
  public static final String TOKEN_PREFIX = "Bearer ";

  public TeleTan createTeleTan(String token) {
    return verificationServerClient.createTeleTan(TOKEN_PREFIX + token);
  }

}
