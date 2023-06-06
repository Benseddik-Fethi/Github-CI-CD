package fr.benseddik.planning.security;

import fr.benseddik.planning.config.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpringSecurityAuditorAwareTest {
    private SpringSecurityAuditorAware auditorAware;

    @BeforeEach
    void setup() {
        auditorAware = new SpringSecurityAuditorAware();
    }

    @Test
    void testGetCurrentAuditor_WithCurrentUser_ReturnsUser() {
        String username = "testuser";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        assertEquals(Optional.of(username), currentAuditor);
    }

    @Test
    void testGetCurrentAuditor_WithAnonymousUser_ReturnsAnonymous() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser", authorities);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        assertEquals(Optional.of(Constants.ANONYMOUS), currentAuditor);
    }

    @Test
    void testGetCurrentAuditor_WithNullUser_ReturnsEmptyOptional() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        assertEquals(Optional.empty(), currentAuditor);
    }
}