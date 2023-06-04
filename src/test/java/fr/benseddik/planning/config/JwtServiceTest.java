package fr.benseddik.planning.config;

import fr.benseddik.planning.domain.User;
import fr.benseddik.planning.domain.enumeration.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@test.fr");
        user.setRoles(List.of(Role.ROLE_USER));

        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", 600000L);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpirationTime", 1200000L);
    }

    @Test
     void testGenerateToken() {
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);
        assertEquals("test@test.fr", username);
    }

    @Test
    void generateRefreshToken() {
        String token = jwtService.generateRefreshToken(user);
        String username = jwtService.extractUsername(token);
        assertEquals("test@test.fr", username);
    }

    @Test
    void isTokenValid() {
        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(token, user);
        assertTrue(isValid);
    }
}