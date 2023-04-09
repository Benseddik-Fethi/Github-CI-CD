package fr.benseddik.planning.domain;

import fr.benseddik.planning.domain.enumeration.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token implements Serializable {


  @Serial
  private static final long serialVersionUID = -5805930154209206158L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
  @SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1, initialValue = 1000)
  @Column(name = "token_id", nullable = false)
  private Integer id;

  @Column(unique = true)
  private String userToken;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType = TokenType.BEARER;

  private boolean isRevoked;

  private boolean isExpired;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
