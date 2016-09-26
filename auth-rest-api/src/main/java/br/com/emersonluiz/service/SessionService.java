package br.com.emersonluiz.service;

import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/sessions")
public interface SessionService {

    @POST
    @Produces("application/json")
    Response create(@Context UriInfo uriInfo, @Context HttpHeaders headers);

    @DELETE
    @Path("/{token}")
    @Produces("application/json")
    Response delete(@PathParam("token") UUID token);

    @GET
    @Path("/{token}")
    @Produces("application/json")
    Response findOne(@PathParam("token") UUID token);

}
