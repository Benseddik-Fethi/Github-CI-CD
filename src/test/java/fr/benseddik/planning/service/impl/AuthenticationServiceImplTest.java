package fr.benseddik.planning.service.impl;

import fr.benseddik.planning.config.JwtService;
import fr.benseddik.planning.domain.User;
import fr.benseddik.planning.domain.enumeration.Role;
import fr.benseddik.planning.dto.request.AuthenticationRequest;
import fr.benseddik.planning.dto.request.RegisterRequest;
import fr.benseddik.planning.dto.response.AuthenticationResponse;
import fr.benseddik.planning.repository.IUserRepository;
import fr.benseddik.planning.service.ITokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ITokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private RegisterRequest registerRequest;
    private User user;
    private final String jwtToken = "jwtToken";
    private final String refreshToken = "refreshToken";
    private AuthenticationResponse authResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("John", "Doe", "johndoe@example.com", "password", List.of(Role.ROLE_USER));
        user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .roles(registerRequest.getRole())
                .build();
        authResponse = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Test
    void register() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        AuthenticationResponse result = authenticationService.register(registerRequest);

        verify(tokenService, times(1)).saveUserToken(user, jwtToken);
        assertEquals(authResponse, result);
    }

    @Test
    void authenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("johndoe@example.com", "password");
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        AuthenticationResponse result = authenticationService.authenticate(authenticationRequest);

        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        verify(tokenService, times(1)).revokeAllUserTokens(user);
        verify(tokenService, times(1)).saveUserToken(user, jwtToken);
        assertEquals(authResponse, result);
    }

    @Test
    void refreshToken() throws IOException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken);

        HttpServletResponse mockResponse = new MockHttpServletResponse();

        when(jwtService.extractUsername(refreshToken)).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        authenticationService.refreshToken(mockRequest, mockResponse);

        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(jwtService, times(1)).isTokenValid(refreshToken, user);
        verify(jwtService, times(1)).generateToken(user);
        verify(tokenService, times(1)).revokeAllUserTokens(user);
        verify(tokenService, times(1)).saveUserToken(user, jwtToken);
    }

}