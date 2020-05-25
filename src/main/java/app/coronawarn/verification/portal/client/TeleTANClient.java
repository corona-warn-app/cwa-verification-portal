package app.coronawarn.verification.portal.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "teleTANClient", url = "${uri.endpoint.verification.service.teletan}")
public interface TeleTANClient {
  @PostMapping()
  @Headers({
    "Accept: application/json",
    "Content-Type: application/json"
  })
  String result();
}
