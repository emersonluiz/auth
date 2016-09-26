package br.com.emersonluiz.auth.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;


public class PreAuthenticatedBearerTokenProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    /**
     * Return the bearer token.
     */
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {

        Object principal = null;
        Authentication authentication = new BearerTokenExtractor().extract(request);
        if (authentication != null) {
            principal = authentication.getPrincipal() == null ? null : authentication.getPrincipal();
        }
        return principal;
    }

    @Override
    /**
     * There is no generic way to get the credentials, use a dummy value
     */
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}
