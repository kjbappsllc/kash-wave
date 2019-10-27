package io.kw.security.cdi;

import org.keycloak.adapters.installed.KeycloakInstalled;

import javax.enterprise.inject.Produces;

public class KeycloakFactory {
    @Produces
    public KeycloakInstalled getKeycloakInstance() {
        return new KeycloakInstalled();
    }
}
