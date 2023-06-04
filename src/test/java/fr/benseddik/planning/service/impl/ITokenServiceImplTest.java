package fr.benseddik.planning.service.impl;

import fr.benseddik.planning.domain.Token;
import fr.benseddik.planning.domain.User;
import fr.benseddik.planning.domain.enumeration.Role;
import fr.benseddik.planning.domain.enumeration.TokenType;
import fr.benseddik.planning.repository.ITokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ITokenServiceImplTest {

    @Mock
    private ITokenRepository tokenRepository;

    @InjectMocks
    private ITokenServiceImpl tokenService;

    private User user;
    private Token token;
    private String jwtToken = "jwtToken";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("johndoe@example.com")
                .password("password")
                .roles(List.of(Role.ROLE_USER))
                .build();
        token = Token.builder()
                .user(user)
                .userToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
    }

    @Test
    void saveUserToken() {
        tokenService.saveUserToken(user, jwtToken);
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokenRepository, times(1)).save(tokenCaptor.capture());
        Token capturedToken = tokenCaptor.getValue();
        assertEquals(user, capturedToken.getUser());
        assertEquals(jwtToken, capturedToken.getUserToken());
    }

    @Test
    void revokeAllUserTokens() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(token);
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(tokens);
        tokenService.revokeAllUserTokens(user);
        token.setExpired(true);
        token.setRevoked(true);
        verify(tokenRepository, times(1)).saveAll(tokens);
    }

    @Test
    void revokeAllUserTokens_emptyTokens() {
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(new ArrayList<>());
        tokenService.revokeAllUserTokens(user);
        verify(tokenRepository, never()).saveAll(anyList());
    }
}