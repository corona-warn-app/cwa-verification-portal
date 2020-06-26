package app.coronawarn.verification.portal.client;

import app.coronawarn.verification.portal.exception.VerificationPortalException;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

@Configuration
@RequiredArgsConstructor
public class VerificationServerClientConfig {

  @Value("${cwa-verification-server.ssl.enabled}")
  private boolean enabled;
  @Value("${cwa-verification-server.ssl.one-way}")
  private boolean oneWay;
  @Value("${cwa-verification-server.ssl.two-way}")
  private boolean twoWay;
  @Value("${cwa-verification-server.ssl.hostname-verify}")
  private boolean hostnameVerify;
  @Value("${cwa-verification-server.ssl.key-store}")
  private String keyStorePath;
  @Value("${cwa-verification-server.ssl.key-store-password}")
  private char[] keyStorePassword;
  @Value("${cwa-verification-server.ssl.trust-store}")
  private String trustStorePath;
  @Value("${cwa-verification-server.ssl.trust-store-password}")
  private char[] trustStorePassword;

  /**
   * Configure the client dependent on the ssl properties.
   *
   * @return an Apache Http Client with or without SSL features
   */
  @Bean
  public Client client() {
    if (enabled) {
      return new ApacheHttpClient(
        HttpClientBuilder
          .create()
          .setSSLContext(getSslContext())
          .setSSLHostnameVerifier(getSslHostnameVerifier())
          .build()
      );
    }
    return new ApacheHttpClient(HttpClientBuilder
      .create()
      .setSSLHostnameVerifier(getSslHostnameVerifier())
      .build());
  }

  private SSLContext getSslContext() {
    try {
      SSLContextBuilder builder = SSLContextBuilder
        .create();
      if (oneWay) {
        builder.loadTrustMaterial(ResourceUtils.getFile(trustStorePath),
          trustStorePassword);
      }
      if (twoWay) {
        builder.loadKeyMaterial(ResourceUtils.getFile(keyStorePath),
          keyStorePassword,
          keyStorePassword);
      }
      return builder.build();
    } catch (IOException | GeneralSecurityException e) {
      throw new VerificationPortalException(HttpStatus.INTERNAL_SERVER_ERROR, "The SSL context could not be loaded.");
    }
  }

  private HostnameVerifier getSslHostnameVerifier() {
    return hostnameVerify ? new DefaultHostnameVerifier() : new NoopHostnameVerifier();
  }

}
