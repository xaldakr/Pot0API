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

    private Integer remitente;

    @NotNull
    private int idTicket;

    // Constructor vacío
    public Notificacion() {}

    // Constructor con todos los atributos
    public Notificacion(int idNotificacion, String dato, String urlArchivo, boolean notificarCliente, Date fecha, boolean autogenerada, Integer remitente, int idTicket) {
        this.idNotificacion = idNotificacion;
        this.dato = dato;
        this.urlArchivo = urlArchivo;
        this.notificarCliente = notificarCliente;
        this.fecha = fecha;
        this.autogenerada = autogenerada;
        this.remitente = remitente;
        this.idTicket = idTicket;
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

    public Integer getRemitente() {
        return remitente;
    }

    public void setRemitente(Integer remitente) {
        this.remitente = remitente;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }
}
