package uce.edu.ec.tienda.application.representation;

import java.util.List;

public class VentasRepresentation {

    private Integer id;
    private Integer numeroVenta;
    private Integer cedulaCliente;
    private Float totalVenta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(Integer numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public Integer getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(Integer cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public Float getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(Float totalVenta) {
        this.totalVenta = totalVenta;
    }

    private List<LinkDto> links;

    public List<LinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDto> links) {
        this.links = links;
    }
}
