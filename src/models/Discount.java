package models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Discount {
    
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    
    private int id;
    private String nombre;
    private boolean esPorcentual;  // true si es porcentaje, false si es monto fijo
    private double valor;          // porcentaje (0-100) o monto fijo
    private LocalDate fechaInicio;
    private LocalDate fechaFin;    // null si no tiene fecha de fin
    private String codigoPromo;    // opcional, para descuentos con código

    public Discount(String nombre, boolean esPorcentual, double valor, 
                   LocalDate fechaInicio, LocalDate fechaFin, String codigoPromo) {
        this.id = idCounter.getAndIncrement();
        this.nombre = nombre;
        this.esPorcentual = esPorcentual;
        this.valor = valor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.codigoPromo = codigoPromo;
    }

    public Discount(String nombre, boolean esPorcentual, double valor, LocalDate fechaInicio) {
        this(nombre, esPorcentual, valor, fechaInicio, null, null);
    }
    
    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaInicio) && (fechaFin == null || !hoy.isAfter(fechaFin));
    }
    
    public double calcularDescuento(double subtotal) {
        if (esPorcentual) {
            return subtotal * (valor / 100.0);
        } else {
            return Math.min(valor, subtotal); // El descuento no puede ser mayor que el subtotal
        }
    }
    
    public boolean validarCodigo(String codigo) {
        return codigoPromo != null && codigoPromo.equals(codigo);
    }
    
    // Getters y setters
    
    public int getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public boolean isEsPorcentual() {
        return esPorcentual;
    }
    
    public void setEsPorcentual(boolean esPorcentual) {
        this.esPorcentual = esPorcentual;
    }
    
    public double getValor() {
        return valor;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public String getCodigoPromo() {
        return codigoPromo;
    }
    
    public void setCodigoPromo(String codigoPromo) {
        this.codigoPromo = codigoPromo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discount)) return false;
        Discount discount = (Discount) o;
        return id == discount.id &&
               esPorcentual == discount.esPorcentual &&
               Double.compare(discount.valor, valor) == 0 &&
               Objects.equals(nombre, discount.nombre) &&
               Objects.equals(fechaInicio, discount.fechaInicio) &&
               Objects.equals(fechaFin, discount.fechaFin) &&
               Objects.equals(codigoPromo, discount.codigoPromo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, esPorcentual, valor, fechaInicio, fechaFin, codigoPromo);
    }
    
    @Override
    public String toString() {
        return "Discount{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + (esPorcentual ? "Porcentual" : "Monto fijo") +
                ", valor=" + (esPorcentual ? valor + "%" : "₡" + valor) +
                ", vigencia=" + fechaInicio + (fechaFin != null ? " hasta " + fechaFin : " sin fecha de fin") +
                (codigoPromo != null ? ", código='" + codigoPromo + '\'' : "") +
                '}';
    }
}