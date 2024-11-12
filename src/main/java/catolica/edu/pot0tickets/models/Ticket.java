package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "idUsuario")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_encargado", referencedColumnName = "idUsuario")
    private Usuario encargado;

    @OneToMany(mappedBy = "ticket")
    private List<Archivo> archivos;

    @OneToMany(mappedBy = "ticket")
    private List<Notificacion> notificaciones;

    // Constructor vacío
    public Ticket() {}

    // Constructor con todos los atributos
    public Ticket(int idTicket, String estado, String descripcion, String prioridad, String servicio, boolean resuelta, Date fecha, Usuario cliente, Usuario encargado) {
        this.idTicket = idTicket;
        this.estado = estado;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.servicio = servicio;
        this.resuelta = resuelta;
        this.fecha = fecha;
        this.cliente = cliente;
        this.encargado = encargado;
    }
    public Ticket( String estado, String descripcion, String prioridad, String servicio, boolean resuelta, Date fecha, Usuario cliente, Usuario encargado) {

        this.estado = estado;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.servicio = servicio;
        this.resuelta = resuelta;
        this.fecha = fecha;
        this.cliente = cliente;
        this.encargado = encargado;
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

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getEncargado() {
        return encargado;
    }

    public void setEncargado(Usuario encargado) {
        this.encargado = encargado;
    }
}
