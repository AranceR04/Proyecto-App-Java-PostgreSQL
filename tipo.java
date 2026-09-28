package Clase;

public class Vehiculo {
    private int id;
    private String marca;
    private String modelo;
    private int precio;
    private boolean disponible;
    private String combustible;

    public Vehiculo(int id, String marca, String modelo, String combustible, int precio, boolean disponible) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.disponible = disponible;
        this.combustible = combustible;
    }

    public int getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getPrecio() {
        return precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    @Override
    public String toString() {
        return marca + " " + modelo;
    }
}
