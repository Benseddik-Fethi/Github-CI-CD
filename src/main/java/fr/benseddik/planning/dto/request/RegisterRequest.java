package fr.benseddik.planning.dto.request;

import fr.benseddik.planning.domain.enumeration.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private List<Role> role;
}
