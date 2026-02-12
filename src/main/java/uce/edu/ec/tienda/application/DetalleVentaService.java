package uce.edu.ec.tienda.application;

import jakarta.inject.Inject;
import uce.edu.ec.tienda.application.representation.DetalleVentaRepresentation;
import uce.edu.ec.tienda.domain.DetalleVenta;
import uce.edu.ec.tienda.infraestructure.DetalleVentaRepository;

public class DetalleVentaService {

    @Inject
    private DetalleVentaRepository detalleVentaRepository;

    private DetalleVenta mapperToDetalleVenta(DetalleVentaRepresentation detalleVentaRepresentation) {
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(detalleVentaRepresentation.getId());
        detalleVenta.setCantidad(detalleVentaRepresentation.getCantidad());
        detalleVenta.setPrecioUnitario(detalleVentaRepresentation.getPrecioUnitario());
        detalleVenta.setSubtotal(detalleVentaRepresentation.getSubtotal());
        detalleVenta.setIdProducto(detalleVentaRepresentation.getIdProducto());

        return detalleVenta;
    }

    private DetalleVentaRepresentation mapperToDetalleVentaRepresentation(DetalleVenta detalleVenta) {
        DetalleVentaRepresentation detalleVentarRepresentation = new DetalleVentaRepresentation();
        detalleVentarRepresentation.setId(detalleVenta.getId());
        detalleVentarRepresentation.setCantidad(detalleVenta.getCantidad());
        detalleVentarRepresentation.setPrecioUnitario(detalleVenta.getPrecioUnitario());
        detalleVentarRepresentation.setSubtotal(detalleVenta.getSubtotal());
        detalleVentarRepresentation.setIdProducto(detalleVenta.getIdProducto());

        return detalleVentarRepresentation;
    }

}
