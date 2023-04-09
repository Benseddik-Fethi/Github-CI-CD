package fr.benseddik.planning.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fethi
 * @date 08/04/2023
 * @time 08:42
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("intern")
public class Intern extends User implements Serializable {


    @Serial
    private static final long serialVersionUID = 3772868487585007577L;

    @NotBlank(message = "arrival date is required")
    @Column(name = "arrival_date")
    private Date arrivalDate;

    @Column(name = "release_date")
    private Date releaseDate;

    @NotNull(message = "archive is required")
    @Column(name = "archive")
    private boolean archive;

}
