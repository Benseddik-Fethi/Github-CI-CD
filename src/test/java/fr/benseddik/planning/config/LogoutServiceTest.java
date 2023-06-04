package fr.benseddik.planning.config;

import fr.benseddik.planning.domain.Token;
import fr.benseddik.planning.repository.ITokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @Mock
    private ITokenRepository tokenRepository;

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private Token storedToken;

    @Test
     void testLogout() {

        when(request.getHeader("Authorization")).thenReturn("Bearer jwt-token");
        when(tokenRepository.findByUserToken(anyString())).thenReturn(Optional.of(storedToken));

        logoutService.logout(request, response, authentication);

        verify(storedToken).setExpired(true);
        verify(storedToken).setRevoked(true);
        verify(tokenRepository).save(storedToken);
    }

    @Test
     void testLogoutNoAuthHeader() {

        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByUserToken(anyString());
        verify(storedToken, never()).setExpired(true);
        verify(storedToken, never()).setRevoked(true);
        verify(tokenRepository, never()).save(storedToken);
    }

    @Test
     void testLogoutInvalidAuthHeader() {

        when(request.getHeader("Authorization")).thenReturn("Invalid jwt-token");

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByUserToken(anyString());
        verify(storedToken, never()).setExpired(true);
        verify(storedToken, never()).setRevoked(true);
        verify(tokenRepository, never()).save(storedToken);
    }

    @Test
     void testLogoutNoTokenFound() {

        when(request.getHeader("Authorization")).thenReturn("Bearer jwt-token");
        when(tokenRepository.findByUserToken(anyString())).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(storedToken, never()).setExpired(true);
        verify(storedToken, never()).setRevoked(true);
        verify(tokenRepository, never()).save(storedToken);
    }
}