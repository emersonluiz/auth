package br.com.emersonluiz.auth.exception;

import java.util.UUID;

@SuppressWarnings("serial")
public class AuthenticationSessionNotFoundException extends Exception {

    public AuthenticationSessionNotFoundException(String message) {
        super(message);
    }

    public AuthenticationSessionNotFoundException(UUID authenticationSessionId) {
        super(String.format("authentication details id '%s' not found", authenticationSessionId));
    }

}
