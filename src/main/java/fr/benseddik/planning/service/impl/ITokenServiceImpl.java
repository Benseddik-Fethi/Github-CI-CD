package fr.benseddik.planning.service.impl;

import fr.benseddik.planning.domain.Token;
import fr.benseddik.planning.domain.User;
import fr.benseddik.planning.domain.enumeration.TokenType;
import fr.benseddik.planning.repository.ITokenRepository;
import fr.benseddik.planning.service.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author fethi
 * @date 04/04/2023
 * @time 05:02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ITokenServiceImpl implements ITokenService {
    private final ITokenRepository tokenRepository;

    @Override
    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .userToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
