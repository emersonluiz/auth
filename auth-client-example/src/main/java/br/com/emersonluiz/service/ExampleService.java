package br.com.emersonluiz.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.security.access.prepost.PreAuthorize;

import br.com.emersonluiz.model.Example;
import br.com.emersonluiz.security.PermissionRules;

@Path("/examples")
public interface ExampleService {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @PreAuthorize(PermissionRules.CREATE_EXAMPLE)
    public Response create(@Context UriInfo uriInfo, Example example);

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @PreAuthorize("isAuthenticated()")
    public Response get(@PathParam("id") int id);

    @GET
    @Produces("text/plain")
    public Response get();
}
