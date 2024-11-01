package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    @NotNull
    @Size(max = 50)
    private String nombre;

    @NotNull
    @Size(max = 50)
    private String apellido;

    @NotNull
    @Size(max = 20)
    private String telefono;

    @NotNull
    @Email
    @Size(max = 250)
    private String email;

    @NotNull
    @Size(max = 260)
    private String contrasena;

    @NotNull
    @Size(max = 20)
    private String telContacto;

    @NotNull
    @Column(name = "id_rol")
    private int idRol;

    // Constructor vacío
    public Usuario() {}

    // Constructor con todos los atributos
    public Usuario(int idUsuario, String nombre, String apellido, String telefono, String email, String contrasena, String telContacto, int idRol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.contrasena = contrasena;
        this.telContacto = telContacto;
        this.idRol = idRol;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelContacto() {
        return telContacto;
    }

    public void setTelContacto(String telContacto) {
        this.telContacto = telContacto;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

}
