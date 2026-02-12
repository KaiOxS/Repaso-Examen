package uce.edu.ec.tienda.infraestructure;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import uce.edu.ec.tienda.domain.Producto;

@ApplicationScoped
public class ProductoRepository implements PanacheRepository<Producto> {
    public Producto findByCodigoBarras(String codigoBarras) {
        return find("codigoBarras", codigoBarras).firstResult();
    }
}
