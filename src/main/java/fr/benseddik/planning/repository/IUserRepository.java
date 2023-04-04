package fr.benseddik.planning.repository;

import fr.benseddik.planning.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

}
