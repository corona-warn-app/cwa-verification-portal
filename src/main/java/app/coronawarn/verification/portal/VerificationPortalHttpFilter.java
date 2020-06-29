package app.coronawarn.verification.portal;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VerificationPortalHttpFilter implements Filter {

  private static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";

  @Value("${route.host}")
  private String routeHost;

  @Value("${route.port}")
  private String routePort;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (isHostHeaderValid(request)) {
      filterChain.doFilter(request, response);
    } else {
      log.warn("Invalid Host Header");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getOutputStream().flush();
      response.getOutputStream().println("Bad Request");
    }
  }

  private boolean isHostHeaderValid(final HttpServletRequest request) {
    final String host = request.getHeader(HttpHeaders.HOST);
    final String xForwardedHost = request.getHeader(X_FORWARDED_HOST_HEADER);
    if (xForwardedHost != null || host == null) {
      return false;
    } else {
      return isHostEqualsToRoute(host);
    }
  }

  private boolean isHostEqualsToRoute(final String host) {
    return host.equals(routeHost) || host.equals(routeHost + ":" + routePort);
  }

}
