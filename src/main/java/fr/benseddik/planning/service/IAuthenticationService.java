package fr.benseddik.planning.service;

import fr.benseddik.planning.dto.request.AuthenticationRequest;
import fr.benseddik.planning.dto.request.RegisterRequest;
import fr.benseddik.planning.dto.response.AuthenticationResponse;

/**
 * @author fethi
 * @date 04/04/2023
 * @time 04:57
 */
public interface IAuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
