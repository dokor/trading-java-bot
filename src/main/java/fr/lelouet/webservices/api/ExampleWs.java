package fr.lelouet.webservices.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.coreoz.plume.jersey.security.permission.PublicApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.lelouet.webservices.api.data.Test;

@Path("/example")
@Tag(name = "example", description = "Manage exemple web-services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PublicApi
@Singleton
public class ExampleWs {

    @Inject
    public ExampleWs() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/test/{name}")
    @Operation(description = "Example web-service")
    public Test test(@Parameter(required = true) @PathParam("name") String name) {
        return new Test("hello " + name + "\n");
    }

}
