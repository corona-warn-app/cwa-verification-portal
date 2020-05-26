package app.coronawarn.verification.portal.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "cwa-verification-server", url = "${cwa-verification-server.url}", primary = false)
public interface VerificationServerFeignClient extends TeleTanClientSI {
}
