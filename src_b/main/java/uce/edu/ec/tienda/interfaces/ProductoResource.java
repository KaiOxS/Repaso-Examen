package uce.edu.ec.tienda.interfaces;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import uce.edu.ec.tienda.application.ProductoService;
import uce.edu.ec.tienda.application.representation.LinkDto;
import uce.edu.ec.tienda.application.representation.ProductoRepresentation;

@Path("/productos")
public class ProductoResource {

    @Inject
    private ProductoService productoService;

    @Context
    private UriInfo uriInfo;

    private ProductoRepresentation construirLinks(ProductoRepresentation productoRepresentation) {
        String self = this.uriInfo.getBaseUriBuilder().path(ProductoResource.class).build().toString();
        productoRepresentation.setLinks(List.of(new LinkDto(self, "self")));
        return productoRepresentation;
    }

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response guardar(ProductoRepresentation productoRepresentation) {
        ProductoRepresentation productoRepresentation1 = this.productoService.crear(productoRepresentation);
        return Response.status(Response.Status.CREATED).entity(productoRepresentation1).build();
    }

}
