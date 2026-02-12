package uce.edu.ec.tienda.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.tienda.application.representation.VentasRepresentation;
import uce.edu.ec.tienda.domain.DetalleVenta;
import uce.edu.ec.tienda.domain.Producto;
import uce.edu.ec.tienda.domain.Ventas;
import uce.edu.ec.tienda.infraestructure.ProductoRepository;
import uce.edu.ec.tienda.infraestructure.VentasRepository;

@ApplicationScoped
public class VentasService {

    @Inject
    private VentasRepository ventasRepository;
    @Inject
    ProductoRepository productoRepository;

    private Ventas mapperToVentas(VentasRepresentation ventasRepresentation) {
        Ventas ventas = new Ventas();
        ventas.setId(ventasRepresentation.getId());
        ventas.setNumeroVenta(ventasRepresentation.getNumeroVenta());
        ventas.setCedulaCliente(ventasRepresentation.getCedulaCliente());
        ventas.setTotalVenta(ventasRepresentation.getTotalVenta());

        return ventas;
    }

    private VentasRepresentation mapperToVentasRepresentation(Ventas ventas) {
        VentasRepresentation ventasRepresentation = new VentasRepresentation();
        ventasRepresentation.setId(ventas.getId());
        ventasRepresentation.setNumeroVenta(ventas.getNumeroVenta());
        ventasRepresentation.setCedulaCliente(ventas.getCedulaCliente());
        ventasRepresentation.setTotalVenta(ventas.getTotalVenta());

        return ventasRepresentation;
    }

    @Transactional
    public void createVenta(Ventas venta) {
        double totalVenta = 0.0;

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = productoRepository.findByCodigoBarras(detalle.getCodigoBarras());

            if (producto == null) {
                throw new RuntimeException("Producto no encontrado: " + detalle.getCodigoBarras());
            }

            // 2. Validar Stock Backend (Seguridad) [cite: 124]
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            // 3. Descontar Stock y guardar Producto
            producto.setStock(producto.getStock() - detalle.getCantidad());
            // No hace falta llamar a persist() explicito si el objeto está managed dentro
            // de @Transactional,
            // pero por seguridad en lógica compleja:
            productoRepository.persist(producto);

            // 4. Asignar datos calculados al detalle [cite: 134]
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(detalle.getCantidad() * producto.getPrecio());
            detalle.setVenta(venta); // Vincular relación bidireccional

            // 5. Sumar al total general [cite: 135]
            totalVenta += detalle.getSubtotal();
        }

        venta.setTotalVenta((float) totalVenta);
        ventasRepository.persist(venta);
    }
}
