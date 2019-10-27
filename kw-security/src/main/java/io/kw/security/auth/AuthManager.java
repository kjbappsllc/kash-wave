package io.kw.security.auth;

import org.keycloak.adapters.installed.KeycloakInstalled;
import org.keycloak.representations.AccessToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;

@ApplicationScoped
public class AuthManager {

    @Inject
    KeycloakInstalled keycloak;

    private AccessToken accessToken;

    public void login() {
        keycloak.setLocale(Locale.ENGLISH);
        try {
            keycloak.loginDesktop();
            accessToken = keycloak.getToken();
        } catch(Exception e) {
            System.out.println("Login Failed");
        }
    }

    public void logout() {
        try {
            keycloak.logout();
        } catch(Exception e) {
            System.out.println("Logout Failed");
        }
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }
}
