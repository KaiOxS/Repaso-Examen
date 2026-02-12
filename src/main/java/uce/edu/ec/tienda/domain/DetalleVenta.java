package uce.edu.ec.tienda.domain;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Detalle")
@SequenceGenerator(name = "detalle_seq", sequenceName = "detalle_secuencia", allocationSize = 1)
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_seq")
    private Integer id;

    private Integer cantidad;

    // Recomendación: Para dinero es mejor BigDecimal o Double, pero Float funciona
    // si tu BD es simple.
    private Float precioUnitario;
    private Float subtotal;

    // --- CORRECCIÓN: SE ELIMINÓ 'private Integer idVenta;' ---
    // El campo idVenta causaba el conflicto porque chocaba con la relación de
    // abajo.

    // Si no tienes una relación @ManyToOne para Producto, puedes dejar este campo.
    // Si agregas una relación con Producto, deberás borrar este campo también.
    private Integer idProducto;

    // Relación que controla la columna "idVenta"
    @ManyToOne
    @JoinColumn(name = "idVenta")
    @JsonbTransient
    private Ventas venta;

    @Column(name = "codigobarras")
    private String codigoBarras;

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    // --- CORRECCIÓN: Se eliminaron getIdVenta y setIdVenta ---

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Ventas getVenta() {
        return venta;
    }

    public void setVenta(Ventas venta) {
        this.venta = venta;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
}