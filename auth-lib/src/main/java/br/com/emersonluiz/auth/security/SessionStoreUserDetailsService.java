package br.com.emersonluiz.auth.security;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import br.com.emersonluiz.auth.model.AuthenticationSession;

public class SessionStoreUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>{

    private final static Logger log = LoggerFactory.getLogger(SessionStoreUserDetailsService.class);

    private final AuthenticationSessionRepository authenticationSessionRepository;

    public SessionStoreUserDetailsService(AuthenticationSessionRepository authenticationSessionRepository){
        checkNotNull(authenticationSessionRepository, "authenticationSessionRepository must not be NULL");
        this.authenticationSessionRepository = authenticationSessionRepository;
    }


    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        // get the authentication session id
        UUID authenticationSessionId = getAuthenticationSessionId(token);

        // get the session
        log.debug("get authentication session for id: '{}'", authenticationSessionId);
        AuthenticationSession authenticationSession = getAuthenticationSession(authenticationSessionId);

        // create user details
        return createUserDetails(authenticationSession);
    }

    private UserDetails createUserDetails(AuthenticationSession authenticationSession) {
        return PreAuthenticatedUser.getFromAuthenticationSession(authenticationSession);
    }

    private AuthenticationSession getAuthenticationSession(UUID sessionId) {
        try{
            AuthenticationSession authenticationSession = authenticationSessionRepository.findOne(sessionId);
            return authenticationSession;
        }
        catch(Exception e){
            throw new UsernameNotFoundException(String.format("Session id '%s' not found in the authenticationSessionRepository, error: '%s'", sessionId, e.getMessage()));
        }
    }

    private UUID getAuthenticationSessionId(PreAuthenticatedAuthenticationToken token) {
        UUID authenticationSessionId;
        try{
            if(token.getPrincipal() == null){
                throw new Exception("The authentication token has no principle!");
            }
            authenticationSessionId = UUID.fromString((String) token.getPrincipal());
        }
        catch(Exception e){
            throw new UsernameNotFoundException(String.format("Failed to get the authentication session id from the authentication token, error: '%s'", e.getMessage()));
        }

        return authenticationSessionId;
    }

}