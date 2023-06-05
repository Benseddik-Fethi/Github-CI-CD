package fr.benseddik.planning.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.benseddik.planning.domain.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {


    @Serial
    private static final long serialVersionUID = -5846629789020830989L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1, initialValue = 1000)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @NotBlank(message = "Firstname is mandatory")
    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;

    @NotBlank(message = "Lastname is mandatory")
    @Column(name = "lastname", nullable = false, length = 50)
    private String lastname;

    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", nullable = false, length = 50, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 8, max = 60)
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    public void prePersist() {
        uuid = java.util.UUID.randomUUID();
    }
}
