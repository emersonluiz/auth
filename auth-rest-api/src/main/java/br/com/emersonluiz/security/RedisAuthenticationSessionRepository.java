package br.com.emersonluiz.security;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import br.com.emersonluiz.auth.exception.AuthenticationSessionNotFoundException;
import br.com.emersonluiz.auth.exception.AuthenticationSessionRepositoryException;
import br.com.emersonluiz.auth.model.AuthenticationSession;

public class RedisAuthenticationSessionRepository {

    private final static Logger log = LoggerFactory.getLogger(RedisAuthenticationSessionRepository.class);

    private final RedisTemplate<UUID, AuthenticationSession> template;

    public RedisAuthenticationSessionRepository(RedisTemplate<UUID, AuthenticationSession> template){
        this.template = template;
    }

    public UUID create(AuthenticationSession session) throws Exception {
        // create new session id
        UUID sessionId = UUID.randomUUID();

        // insert session
        try {
            template.opsForValue().set(sessionId, session);
            template.expire(sessionId, 120, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new AuthenticationSessionRepositoryException(String.format("Failed to create a new authentication session , error: '%s'", e.getMessage()));
        }

        return sessionId;
    }

    public String getUser(UUID token) {
        return template.opsForValue().get(token).getUsername();
    }

    public void delete(UUID id) throws Exception {
        // delete session
        try {
            template.delete(id);
        } catch (Exception e) {
            throw new AuthenticationSessionRepositoryException(String.format("Failed to delete authentication session '%s', error: '%s'", id, e.getMessage()));
        }
    }

    public AuthenticationSession findOne(UUID id) throws AuthenticationSessionNotFoundException, AuthenticationSessionRepositoryException {

        // get the authentication session
        AuthenticationSession authenticationSession = null;
        try {
            authenticationSession = template.opsForValue().get(id);
        } catch (Exception e) {
            throw new AuthenticationSessionRepositoryException(String.format("Failed to get the value of authentication session '%s', error: '%s'", id, e.getMessage()));
        }

        // session not found
        if (authenticationSession == null) {
            throw new AuthenticationSessionNotFoundException(id);
        }
        // session found
        else {
            log.debug("authentication session '{}' found", id);
        }

        return authenticationSession;
    }

}
