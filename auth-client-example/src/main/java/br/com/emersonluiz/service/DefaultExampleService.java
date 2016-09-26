package br.com.emersonluiz.service;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.com.emersonluiz.model.Example;
import br.com.emersonluiz.util.FailureResponseBuilder;

public class DefaultExampleService implements ExampleService {

    @Override
    public Response create(UriInfo uriInfo, Example example) {
        try {
            example.setId(1);
            String id = String.valueOf(example.getId());
            URI uri = uriInfo.getRequestUriBuilder().path(id).build();
            return Response.created(uri).entity(example).build();
        } catch (Exception e) {
            return new FailureResponseBuilder().toResponse(e);
        }
    }

    @Override
    public Response get(int id) {
        try {
            Example example = new Example();
            example.setDescription("This is a method get");
            example.setId(id);
            return Response.ok(example).build();
        } catch (Exception e) {
            return new FailureResponseBuilder().toResponse(e);
        }
    }

    @Override
    public Response get() {
        return Response.ok().entity("This is public access!").build();
    }

}
