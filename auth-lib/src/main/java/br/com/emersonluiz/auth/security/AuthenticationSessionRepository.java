package br.com.emersonluiz.auth.security;

import java.util.UUID;

import br.com.emersonluiz.auth.exception.AuthenticationSessionNotFoundException;
import br.com.emersonluiz.auth.exception.AuthenticationSessionRepositoryException;
import br.com.emersonluiz.auth.model.AuthenticationSession;

public interface AuthenticationSessionRepository {

    UUID create(AuthenticationSession session) throws AuthenticationSessionRepositoryException;
    AuthenticationSession findOne(UUID sessionId) throws AuthenticationSessionRepositoryException, AuthenticationSessionNotFoundException;
    void delete(UUID sessionId) throws AuthenticationSessionRepositoryException;
}
