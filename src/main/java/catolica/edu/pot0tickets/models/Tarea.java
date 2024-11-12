package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTarea;

    @NotNull
    @Size(max = 70)
    private String nombre;

    @NotNull
    @Size(max = 250)
    private String info;

    @NotNull
    @Size(max = 20)
    private String prioridad;

    @NotNull
    private String estado;

    private boolean completada;

    @ManyToOne
    @JoinColumn(name = "id_ticket", referencedColumnName = "idTicket")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_encargado", referencedColumnName = "idUsuario")
    private Usuario encargado;

    // Constructor vacío
    public Tarea() {}

    // Constructor con todos los atributos
    public Tarea(int idTarea, String nombre, String info, String prioridad, String estado, boolean completada, Ticket ticket, Usuario encargado) {
        this.idTarea = idTarea;
        this.nombre = nombre;
        this.info = info;
        this.prioridad = prioridad;
        this.estado = estado;
        this.completada = completada;
        this.ticket = ticket;
        this.encargado = encargado;
    }
    public Tarea( String nombre, String info, String prioridad, String estado, boolean completada, Ticket ticket, Usuario encargado) {

        this.nombre = nombre;
        this.info = info;
        this.prioridad = prioridad;
        this.estado = estado;
        this.completada = completada;
        this.ticket = ticket;
        this.encargado = encargado;
    }

    // Getters y Setters
    public int getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(int idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Usuario getEncargado() {
        return encargado;
    }

    public void setEncargado(Usuario encargado) {
        this.encargado = encargado;
    }
}
