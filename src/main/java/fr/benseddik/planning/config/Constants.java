package fr.benseddik.planning.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Applications constants.
 *
 * @author Peter Mollet
 */
public final class Constants {


    private Constants() {}

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String DEFAULT_ROLE = "ROLE_USER";
    public static final String ANONYMOUS = "anonymous";


    //RESPONSES
    public static final String BAD_MESSAGE = "Mauvais mot de passe";
    public static final String BAD_USERNAME_MESSAGE = "Mauvais nom d'utilisateur";
    public static final Object FORBIDDEN_MESSAGE = "Vous n'avez pas les droits pour accéder à cette ressource";
    public static final Object DISABLED_MESSAGE = "Votre compte est désactivé";

}
