package uce.edu.ec.tienda.application;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.ec.tienda.application.representation.ProductoRepresentation;
import uce.edu.ec.tienda.domain.Producto;
import uce.edu.ec.tienda.infraestructure.ProductoRepository;

@ApplicationScoped
public class ProductoService extends PanacheEntityBase {

    @Inject
    private ProductoRepository productoRepository;

    private Producto mapperToProducto(ProductoRepresentation productoRepresentation) {
        Producto producto = new Producto();
        producto.setId(productoRepresentation.getId());
        producto.setNombre(productoRepresentation.getNombre());
        producto.setCodigoBarras(productoRepresentation.getCodigoBarras());
        producto.setCategoria(productoRepresentation.getCategoria());
        producto.setStock(productoRepresentation.getStock());
        producto.setPrecio(productoRepresentation.getPrecio());

        return producto;
    }

    private ProductoRepresentation mapperToProductoRepresentation(Producto producto) {
        ProductoRepresentation productoRepresentation = new ProductoRepresentation();
        productoRepresentation.setId(producto.getId());
        productoRepresentation.setNombre(producto.getNombre());
        productoRepresentation.setCodigoBarras(producto.getCodigoBarras());
        productoRepresentation.setCategoria(producto.getCategoria());
        productoRepresentation.setStock(producto.getStock());
        productoRepresentation.setPrecio(producto.getPrecio());

        return productoRepresentation;
    }

    @Transactional
    public ProductoRepresentation crear(ProductoRepresentation productoRepresentation) {
        Producto producto = this.mapperToProducto(productoRepresentation);

        Producto productoExistente = this.productoRepository.find("codigoBarras", producto.getCodigoBarras()).firstResult();

        if (productoExistente != null) {
            productoExistente.setStock(productoExistente.getStock() + producto.getStock());
            this.productoRepository.persist(productoExistente);
            return this.mapperToProductoRepresentation(productoExistente);
        } else {
            this.productoRepository.persist(producto);
            return this.mapperToProductoRepresentation(producto);
        }
    }
}
