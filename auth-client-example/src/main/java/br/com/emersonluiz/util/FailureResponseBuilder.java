package br.com.emersonluiz.util;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@Provider
public class FailureResponseBuilder implements ExceptionMapper<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(FailureResponseBuilder.class);

    @Override
    public Response toResponse(Exception ex) {
        FailureReason failureReason = new FailureReason();
        failureReason.setFailureReason(ex.getMessage());
        if(ex instanceof AccessDeniedException) {
            failureReason.setFailureReason(ex.getMessage());
            return Response.status(Status.FORBIDDEN).entity(failureReason).type(MediaType.APPLICATION_JSON).build();
        } else if (ex instanceof AuthenticationCredentialsNotFoundException) {
            failureReason.setFailureReason("No authentication information found");
            return Response.status(Status.UNAUTHORIZED).entity(failureReason).type(MediaType.APPLICATION_JSON).build();
        } else if (ex instanceof JsonParseException || ex instanceof UnrecognizedPropertyException || ex instanceof JsonMappingException) {
            return Response.status(Status.BAD_REQUEST).entity(failureReason).type(MediaType.APPLICATION_JSON).build();
        } else if (ex instanceof StatusException) {
            return Response.status(((StatusException) ex).getStatusType()).entity(failureReason).type(MediaType.APPLICATION_JSON).build();
        } else if (ex instanceof ClientErrorException) {
            return parseClientException(failureReason);
        } else if (ex instanceof WebApplicationException) {
            failureReason.setFailureReason(ex.getLocalizedMessage());
            return Response.status(((WebApplicationException) ex).getResponse().getStatus()).entity(failureReason).type(MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).type(MediaType.APPLICATION_JSON).build();
        }
    }

    private Response parseClientException(FailureReason failureReason) {
        String reason = failureReason.getFailureReason();
        String[] splitReason = reason.split(" ");
        int status = 400;
        if(splitReason.length > 1) {
            try {
                status = Integer.parseInt(splitReason[1]);
            } catch(Exception e) {
                logger.warn("Problem getting the ClientErrorException.");
            }
        }
        return Response.status(status).entity(failureReason).type(MediaType.APPLICATION_JSON).build();
    }

}
