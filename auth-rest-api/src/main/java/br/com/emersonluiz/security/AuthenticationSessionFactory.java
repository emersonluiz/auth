package br.com.emersonluiz.security;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.emersonluiz.model.User;
import br.com.emersonluiz.auth.model.AuthenticationSession;

public class AuthenticationSessionFactory {

    private User user;

    public AuthenticationSessionFactory(User user) {
        this.user = user;
    }

    public AuthenticationSession generateAuthenticationSession() {

        UUID userIdRandom = UUID.fromString(user.getId());
        String userName = user.getUserName();

        AuthenticationSession authenticationSession = new AuthenticationSession(userIdRandom, userName);
        List<String> authorities = new ArrayList<>();

        Roles read = Roles.CLIENT_CREATE_EXAMPLE;
        authorities.add(read.getRole());

        authenticationSession.setAuthorities(authorities);
        return authenticationSession;
    }

}
