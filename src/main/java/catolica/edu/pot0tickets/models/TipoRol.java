package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Tipos_Rol")
public class TipoRol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTipoRol;

    @NotNull
    @Size(max = 20)
    private String nombre;

    // Constructor vacío
    public TipoRol() {}

    // Constructor con todos los atributos
    public TipoRol(int idTipoRol, String nombre) {
        this.idTipoRol = idTipoRol;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(int idTipoRol) {
        this.idTipoRol = idTipoRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
