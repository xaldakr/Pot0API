package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

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

    // Relación ManyToOne con Rol
    @ManyToOne
    @JoinColumn(name = "id_rol", referencedColumnName = "idRol", insertable = false, updatable = false)
    private Rol rol;

    @OneToMany(mappedBy = "cliente")
    private List<Ticket> ticketsCliente;

    @OneToMany(mappedBy = "encargado")
    private List<Ticket> ticketsEncargado;

    @OneToMany(mappedBy = "encargado")
    private List<Tarea> tareas;

    // Constructor vacío
    public Usuario() {}

    // Constructor con todos los atributos
    public Usuario(int idUsuario, String nombre, String apellido, String telefono, String email, String contrasena, String telContacto, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.contrasena = contrasena;
        this.telContacto = telContacto;
        this.rol = rol;
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return this.nombre + " " + this.apellido;
    }
}
