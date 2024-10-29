package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTicket;

    @NotNull
    @Size(max = 35)
    @Pattern(regexp = "^(CREADO|ASIGNADO|EN ESPERA DE INFORMACIÓN|EN RESOLUCIÓN|RESUELTO)$")
    private String estado;

    @NotNull
    private String descripcion;

    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "^(BAJA|NORMAL|IMPORTANTE|CRÍTICA)$")
    private String prioridad;

    @NotNull
    @Size(max = 250)
    private String servicio;

    private boolean resuelta;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private Integer idEncargado;

    @NotNull
    private int idCliente;

    // Constructor vacío
    public Ticket() {}

    // Constructor con todos los atributos
    public Ticket(int idTicket, String estado, String descripcion, String prioridad, String servicio, boolean resuelta, Date fecha, Integer idEncargado, int idCliente) {
        this.idTicket = idTicket;
        this.estado = estado;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.servicio = servicio;
        this.resuelta = resuelta;
        this.fecha = fecha;
        this.idEncargado = idEncargado;
        this.idCliente = idCliente;
    }

    // Getters y Setters
    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public boolean isResuelta() {
        return resuelta;
    }

    public void setResuelta(boolean resuelta) {
        this.resuelta = resuelta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getIdEncargado() {
        return idEncargado;
    }

    public void setIdEncargado(Integer idEncargado) {
        this.idEncargado = idEncargado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
