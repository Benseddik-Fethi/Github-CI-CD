package fr.benseddik.planning.controller;


import fr.benseddik.planning.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        log.debug("REST request to get current user");
        return ResponseEntity.ok(new User());
    }
}
