package br.com.emersonluiz.service;

import java.util.Base64;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import br.com.emersonluiz.auth.exception.AuthenticationSessionNotFoundException;
import br.com.emersonluiz.auth.model.AuthenticationSession;
import br.com.emersonluiz.auth.security.RedisAuthenticationSessionRepository;
import br.com.emersonluiz.model.User;
import br.com.emersonluiz.repository.UserRepository;
import br.com.emersonluiz.security.AuthenticationSessionFactory;
import br.com.emersonluiz.security.Encryption;
import br.com.emersonluiz.util.FailureResponseBuilder;
import br.com.emersonluiz.util.StatusException;

public class DefaultSessionService implements SessionService {

    private final UserRepository userRepository;
    RedisTemplate<UUID, AuthenticationSession> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DefaultSessionService.class);

    @Inject
    public DefaultSessionService(UserRepository userRepository, RedisTemplate<UUID, AuthenticationSession> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Response create(UriInfo uriInfo, HttpHeaders headers) {
        try {
            String headerAuth = headers.getRequestHeader("Authorization").get(0);

            headerAuth = headerAuth.replaceAll("Basic ", "");
            final String authorization = new String(Base64.getDecoder().decode(headerAuth));

            final String[] user_pass = authorization.split(":");

            if (user_pass.length < 2) {
                logger.error("User or Password invalid.");
                return new FailureResponseBuilder().toResponse(new StatusException(Status.BAD_REQUEST.getStatusCode(), "User or Password invalid."));
            }

            if (user_pass[0].isEmpty()) {
                logger.error("User invalid.");
                return new FailureResponseBuilder().toResponse(new StatusException(Status.BAD_REQUEST.getStatusCode(), "User invalid."));
            }

            if (user_pass[1].isEmpty()) {
                logger.error("Password invalid.");
                return new FailureResponseBuilder().toResponse(new StatusException(Status.BAD_REQUEST.getStatusCode(), "Password invalid."));
            }

            final String password = Encryption.encrypt(user_pass[1]);
            final User user = userRepository.findOne(user_pass[0], password);

            if (user == null) {
                logger.error("User unauthorized.");
                return Response.status(Status.UNAUTHORIZED).entity("User unauthorized").build();
            }

            final AuthenticationSessionFactory authenticationSessionFactory = new AuthenticationSessionFactory(user);
            final AuthenticationSession authenticationSession = authenticationSessionFactory.generateAuthenticationSession();

            final RedisAuthenticationSessionRepository authenticationSessionRepository = new RedisAuthenticationSessionRepository(redisTemplate);
            final UUID token = authenticationSessionRepository.create(authenticationSession);

            final String identification = "{\"token\":\"" + token
                    + "\", \"user\":{\"id\":\"" + user.getId()
                    + "\", \"name\":\"" + user.getName() + "\"}}";

            logger.debug("session '{}' created", token);
            return Response.status(Status.CREATED).entity(identification).build();
        } catch (final Exception e) {
            logger.error(e.getMessage());
            return new FailureResponseBuilder().toResponse(e);
        }
    }

    @Override
    public Response delete(UUID token) {
        final RedisAuthenticationSessionRepository authenticationSessionRepository = new RedisAuthenticationSessionRepository(redisTemplate);
        try {
            authenticationSessionRepository.delete(token);
            logger.debug("session '{}' deleted", token);
            return Response.noContent().build();
        } catch (final Exception e) {
            logger.error(e.getMessage());
            return new FailureResponseBuilder().toResponse(e);
        }
    }

    @Override
    public Response findOne(UUID token) {
        try {
            final RedisAuthenticationSessionRepository authenticationSessionRepository = new RedisAuthenticationSessionRepository(redisTemplate);
            final AuthenticationSession authenticationSession = authenticationSessionRepository.findOne(token);

            final User user = userRepository.findOne(authenticationSession.getUserid().toString());

            final String identification = "{\"token\":\"" + token
                    + "\", \"user\":{\"id\":\"" + user.getId()
                    + "\", \"name\":\"" + user.getName() + "\"}}";

            return Response.ok().entity(identification).build();
        } catch (final AuthenticationSessionNotFoundException e) {
            logger.error("Session not found.");
            return new FailureResponseBuilder().toResponse(new StatusException(Status.NOT_FOUND.getStatusCode(), "Session not found"));
        } catch (final Exception e) {
            logger.error(e.getMessage());
            return new FailureResponseBuilder().toResponse(e);
        }
    }

}
