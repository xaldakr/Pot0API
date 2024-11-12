package catolica.edu.pot0tickets.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Archivos")
public class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArchivo;

    @NotNull
    @Size(max = 400)
    private String url;

    @ManyToOne
    @JoinColumn(name = "id_ticket", referencedColumnName = "idTicket")
    private Ticket ticket;

    // Constructor vacío
    public Archivo() {}

    // Constructor con todos los atributos
    public Archivo(int idArchivo, String url, Ticket ticket) {
        this.idArchivo = idArchivo;
        this.url = url;
        this.ticket = ticket;
    }

    // Getters y Setters
    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
