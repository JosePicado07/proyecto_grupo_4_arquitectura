package models;

import java.util.Objects;

public class Product {
    private int id;
    private String nombre;
    private Double precio;


    public Product(int id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product productos)) return false;
        return getId() == productos.getId() && Objects.equals(getNombre(), productos.getNombre()) && Objects.equals(getPrecio(), productos.getPrecio());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNombre(), getPrecio());
    }

    @Override
    public String toString() {
        return "Productos{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio= ₡" + precio +
                '}';
    }
}
