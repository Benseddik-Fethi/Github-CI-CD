package fr.benseddik.planning.service;

import fr.benseddik.planning.domain.User;

/**
 * @author fethi
 * @date 04/04/2023
 * @time 05:01
 */
public interface ITokenService {
    void saveUserToken(User user, String jwtToken);
    void revokeAllUserTokens(User user);
}
