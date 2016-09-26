package br.com.emersonluiz.auth.security;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class BearerTokenExtractor {

    private final static Logger log = LoggerFactory.getLogger(BearerTokenExtractor.class);
    private static final String ACCESS_TOKEN = "access_token";
    private static final String BEARER_TYPE = "Bearer";

    public Authentication extract(HttpServletRequest request) {

        // extract the token
        String tokenValue = extractToken(request);

        // create the authentication object
        PreAuthenticatedAuthenticationToken authentication = null;
        if (tokenValue != null) {
            authentication = new PreAuthenticatedAuthenticationToken(tokenValue, "");
        }
        return authentication;
    }

    protected String extractToken(HttpServletRequest request) {

        // first check the header...
        String token = extractHeaderToken(request);

        // bearer type allows a request parameter as well
        if (token == null) {
            log.debug("Token not found in headers. Trying request parameters.");
            token = request.getParameter(ACCESS_TOKEN);
            if (token == null) {
                log.debug("Token not found in request parameters. Not an OAuth2 request.");
            }
        }

        return token;
    }

    /**
     * Extract the OAuth bearer token from a header.
     *
     * @param request
     *            The request.
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    protected String extractHeaderToken(HttpServletRequest request) {

        // get the authorization headers
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) {
            // get header value
            String value = headers.nextElement();

            // header value contains the bearer token
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {

                // extract the bearer token
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }

        return null;
    }

}
