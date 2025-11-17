package pe.edu.upeu.poo.clases;

public class Vehiculo {
    private String color;
    private String marca;
    private String modelo;
    private Integer precio;
    private Integer acelerado;
    private Boolean estado = false;

    public Float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Float velocidad) {
        this.velocidad = velocidad;
    }

    private Float velocidad = 0.0f;

    public Vehiculo(String color, String marca, Integer precio, String modelo, Integer acelerado, Boolean estado) {
        this.color = color;
        this.marca = marca;
        this.precio = precio;
        this.modelo = modelo;
        this.acelerado = acelerado;
        this.estado = estado;

    }

    public Vehiculo() {
    }

    public String setColor(Integer num) {
        return color;
    }

    public void getColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getAcelerado() {
        return acelerado;
    }

    public void setAcelerado(Integer acelerado) {
        this.velocidad = this.velocidad + acelerado;
        this.acelerado = acelerado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void encendido() {
        this.velocidad = this.velocidad + 0.1f;
        this.estado = true;
    }

    public void desencendido() {
        this.estado = false;
        this.velocidad = 0.0f;
    }

    @Override
    public String toString() {
        return "Automovil{" +
                "color='" + color + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precio=" + precio +
                ", acelerado=" + acelerado +
                ", estado=" + estado +
                ", velocidad=" + velocidad +
                '}';
    }
}
