package fr.benseddik.planning.security;

import fr.benseddik.planning.error.exception.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {

    private SecurityUtils() {}


    public static Optional<String> getCurrentUserSubOptional() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    public static String getCurrentUserSub() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()))
                .orElseThrow(UsernameNotFoundException::new);
    }


    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails user) {
            return user.getUsername();
        } else if (authentication.getPrincipal() instanceof String username) {
            return username;
        }
        return null;
    }
}
