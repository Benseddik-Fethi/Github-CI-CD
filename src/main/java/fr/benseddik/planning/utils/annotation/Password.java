package fr.benseddik.planning.utils.annotation;


import fr.benseddik.planning.utils.annotation.validator.PasswordConstraintsValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

/**
 * @author fethi
 * @date 03/03/2023
 * @time 13:54
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordConstraintsValidator.class)
public @interface Password {
    String message() default "Password should be valid ";

    Class[] groups() default {};

    Class[] payload() default {};
}
