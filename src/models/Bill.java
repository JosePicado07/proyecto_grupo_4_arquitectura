package models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Bill {


    private static final AtomicInteger idCounter = new AtomicInteger(1);

    private int id;
    private List<Product> productos;
    private double subtotal;
    private double impuestos;
    private double total;
    private int sinpe;
    private String cedulaCliente;
    private LocalDateTime fecha;

    public Bill(List<Product> productos, int sinpe, User currentUser) {
        this.id = idCounter.getAndIncrement();
        this.productos = productos;
        this.subtotal = calcularSubtotal();
        this.impuestos = subtotal * 0.13;
        this.total = subtotal + impuestos;
        this.sinpe = sinpe;
        this.cedulaCliente = currentUser.getCedula();
        this.fecha = LocalDateTime.now();
    }

    private double calcularSubtotal() {
        return productos.stream()
                .mapToDouble(Product::getPrecio)
                .sum();
    }

    public void imprimirFactura() {
        System.out.println("Factura '" + id + "'");
        System.out.println("Fecha: " + fecha);
        System.out.println("Cédula del cliente: " + cedulaCliente);
        System.out.println("Artículos:");
        productos.forEach(product -> System.out.println(product.toString()));
        System.out.println("'' Subtotal: " + subtotal + " ''");
        System.out.println("Impuestos: 13% ''");
        System.out.println("Total: " + total + " ''");
        System.out.println("Número de comprobante SINPE: " + sinpe);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getProductos() {
        return productos;
    }

    public void setProductos(List<Product> productos) {
        this.productos = productos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    public double getTotal() {
        return total;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getSinpe() {
        return sinpe;
    }

    public void setSinpe(int sinpe) {
        this.sinpe = sinpe;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bill bill)) return false;
        return getId() == bill.getId() && Double.compare(getSubtotal(), bill.getSubtotal()) == 0 && Double.compare(getImpuestos(), bill.getImpuestos()) == 0 && Double.compare(getTotal(), bill.getTotal()) == 0 && getSinpe() == bill.getSinpe() && Objects.equals(getProductos(), bill.getProductos());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProductos(), getSubtotal(), getImpuestos(), getTotal(), getSinpe());
    }
}
