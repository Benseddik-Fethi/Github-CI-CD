package fr.benseddik.planning.security;

import fr.benseddik.planning.config.Constants;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        Optional<String> user = SecurityUtils.getCurrentUserSubOptional();
        if(user.isPresent() && (user.get().equals("anonymousUser"))){
                return Optional.of(Constants.ANONYMOUS);

    }
        return user;
    }
}
