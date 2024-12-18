package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNotificacion;

    @NotNull
    @Size(max = 500)
    private String dato;

    @NotNull
    @Size(max = 400)
    private String urlArchivo;

    private boolean notificarCliente;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private boolean autogenerada;

    @ManyToOne
    @JoinColumn(name = "remitente", referencedColumnName = "idUsuario")
    private Usuario remitente;

    @ManyToOne
    @JoinColumn(name = "id_ticket", referencedColumnName = "idTicket")
    private Ticket ticket;

    // Constructor vacío
    public Notificacion() {}

    // Constructor con todos los atributos
    public Notificacion(int idNotificacion, String dato, String urlArchivo, boolean notificarCliente, Date fecha, boolean autogenerada, Usuario remitente, Ticket ticket) {
        this.idNotificacion = idNotificacion;
        this.dato = dato;
        this.urlArchivo = urlArchivo;
        this.notificarCliente = notificarCliente;
        this.fecha = fecha;
        this.autogenerada = autogenerada;
        this.remitente = remitente;
        this.ticket = ticket;
    }

    // Constructor sin id
    public Notificacion( String dato, String urlArchivo, boolean notificarCliente, boolean autogenerada, Usuario remitente, Ticket ticket) {
        this.dato = dato;
        this.urlArchivo = urlArchivo;
        this.notificarCliente = notificarCliente;
        this.fecha = new Date();
        this.autogenerada = autogenerada;
        this.remitente = remitente;
        this.ticket = ticket;
    }

    // Constructor sin id ni archivo
    public Notificacion( String dato, boolean notificarCliente, boolean autogenerada, Usuario remitente, Ticket ticket) {
        this.dato = dato;
        this.notificarCliente = notificarCliente;
        this.fecha = new Date();
        this.autogenerada = autogenerada;
        this.remitente = remitente;
        this.ticket = ticket;
        this.urlArchivo = "No existente";
    }

    // Constructor automático
    public Notificacion( String dato, Usuario remitente, Ticket ticket) {
        this.dato = dato;
        this.notificarCliente = false;
        this.fecha = new Date();
        this.autogenerada = true;
        this.remitente = remitente;
        this.ticket = ticket;
        this.urlArchivo = "No existente";
    }

    // Getters y Setters
    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public boolean isNotificarCliente() {
        return notificarCliente;
    }

    public void setNotificarCliente(boolean notificarCliente) {
        this.notificarCliente = notificarCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isAutogenerada() {
        return autogenerada;
    }

    public void setAutogenerada(boolean autogenerada) {
        this.autogenerada = autogenerada;
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}