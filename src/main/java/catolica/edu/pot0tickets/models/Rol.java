package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRol;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @NotNull
    @Positive
    private int capacidad;

    @NotNull
    @Column(name = "tipo_rol")
    private int tipoRol;

    // Constructor vacío
    public Rol() {}

    // Constructor con todos los atributos
    public Rol(int idRol, String nombre, int capacidad, int tipoRol) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.tipoRol = tipoRol;
    }

    // Getters y Setters
    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(int tipoRol) {
        this.tipoRol = tipoRol;
    }
}
