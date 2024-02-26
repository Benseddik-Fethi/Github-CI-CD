package fr.benseddik.planning.config;

import fr.benseddik.planning.domain.User;
import fr.benseddik.planning.domain.enumeration.Role;
import fr.benseddik.planning.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authConfiguration;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Test
    void userDetailsService() {
        User user = User.builder()
                .email("test@example.com")
                .password("testPassword")
                .firstname("testFirstname")
                .lastname("testLastname")
                .roles(Role.ROLE_USER)
                .build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
        verify(userRepository).findByEmail("test@example.com");
        assertNotNull(userDetails);
    }

    @Test
    void authenticationProvider() {
        User user = User.builder()
                .email("testEmail")
                .password("testPassword")
                .firstname("testFirstname")
                .lastname("testLastname")
                .roles(Role.ROLE_USER)
                .build();
        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertNotNull(userDetailsService);
        assertNotNull(passwordEncoder);
        AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();
        assertNotNull(authenticationProvider);
    }

    @Test
    void authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);
        AuthenticationManager returnedManager = applicationConfig.authenticationManager(authConfiguration);
        assertEquals(authenticationManager, returnedManager);
    }

    @Test
    void passwordEncoder() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
    @Test
    void userDetailsServiceThrowsWhenUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonExistentUser");
        });
        verify(userRepository).findByEmail("nonExistentUser");
    }
}
