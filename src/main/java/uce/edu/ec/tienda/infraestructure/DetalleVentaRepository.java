package uce.edu.ec.tienda.infraestructure;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import uce.edu.ec.tienda.domain.DetalleVenta;

@ApplicationScoped
public class DetalleVentaRepository implements PanacheRepository<DetalleVenta> {

}
