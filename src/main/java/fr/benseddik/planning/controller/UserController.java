package fr.benseddik.planning.controller;

import fr.benseddik.planning.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fethi
 * @date 04/04/2023
 * @time 04:51
 */
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final IUserRepository userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public String getAllUsers() {
        log.info("get all users");
        return "hello";
    }


}
