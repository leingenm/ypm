package xyz.ypmngr.config.spring.security;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class GoogleAuthenticationMediationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @Nullable HttpServletRequest request,
        @Nullable HttpServletResponse response,
        @Nullable FilterChain filterChain
    ) throws ServletException, IOException {
        if (filterChain == null || request == null) {
            throw new IllegalArgumentException("Filter used outside filter chain");
        }
        var header = request.getHeader(AUTHORIZATION);
        if (header == null) {
            filterChain.doFilter(request, response);
        } else if (header.startsWith("Bearer")) {
            var token = header.substring(7);
            var auth = new BearerTokenAuthenticationToken(token);
            auth.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } else {
            throw new InvalidBearerTokenException("Invalid bearer token passed");
        }
    }
}
