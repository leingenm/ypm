package xyz.ypmngr.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TokenService implements AuthService {

    @Override
    public String getToken() throws AuthenticationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof BearerTokenAuthenticationToken bearer) {
            return bearer.getToken();
        } else {
            throw new InvalidBearerTokenException(
                    "Invalid authentication stored in context: " + auth.getClass().getSimpleName()
            );
        }
    }
}
