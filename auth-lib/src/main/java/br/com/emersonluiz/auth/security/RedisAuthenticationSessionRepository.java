package br.com.emersonluiz.auth.security;

import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import br.com.emersonluiz.auth.exception.AuthenticationSessionNotFoundException;
import br.com.emersonluiz.auth.exception.AuthenticationSessionRepositoryException;
import br.com.emersonluiz.auth.model.AuthenticationSession;
import br.com.emersonluiz.auth.security.AuthenticationSessionRepository;

public class RedisAuthenticationSessionRepository implements AuthenticationSessionRepository {

    private final static Logger log = LoggerFactory.getLogger(RedisAuthenticationSessionRepository.class);

    private final RedisTemplate<UUID, AuthenticationSession> template;
    private final ValueOperations<UUID, AuthenticationSession> valueOps;

    @Inject
    public RedisAuthenticationSessionRepository(RedisTemplate<UUID, AuthenticationSession> template){
        this.template = template;
        this.valueOps = template.opsForValue();
    }

    @Override
    public AuthenticationSession findOne(UUID id) throws AuthenticationSessionNotFoundException, AuthenticationSessionRepositoryException {

        // get the authentication session
        AuthenticationSession authenticationSession = null;
        try {
            authenticationSession = valueOps.get(id);
        } catch (Exception e) {
            throw new AuthenticationSessionRepositoryException(
                    String.format(
                            "Failed to get the value of authentication session '%s', error: '%s'",
                            id, e.getMessage()));
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

    @Override
    public UUID create(AuthenticationSession session) throws AuthenticationSessionRepositoryException {

        // create new session id
        UUID sessionId = UUID.randomUUID();

        // insert session
        try {
            valueOps.set(sessionId, session);
        } catch (Exception e) {
            throw new AuthenticationSessionRepositoryException(
                    String.format(
                            "Failed to create a new authentication session , error: '%s'",
                            e.getMessage()));
        }

        return sessionId;
    }

    @Override
    public void delete(UUID id) throws AuthenticationSessionRepositoryException {
        // delete session
        try {
            template.delete(id);
        } catch (Exception e) {
            throw new AuthenticationSessionRepositoryException(
                    String.format(
                            "Failed to delete authentication session '%s', error: '%s'",
                            id, e.getMessage()));
        }
    }

}
