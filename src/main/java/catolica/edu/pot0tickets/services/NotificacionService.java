package catolica.edu.pot0tickets.services;
import catolica.edu.pot0tickets.models.Notificacion;
import catolica.edu.pot0tickets.models.Ticket;
import catolica.edu.pot0tickets.models.Usuario;
import catolica.edu.pot0tickets.repositories.NotificacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    /**
     * Crea una notificación y la guarda en la base de datos.
     *
     * @param mensaje    Contenido de la notificación.
     * @param remitente  ID del usuario remitente (puede ser null).
     * @param idTicket   ID del ticket asociado.
     */
    public void createNotificacion(String mensaje, Usuario remitente, Ticket Ticket) {
        Notificacion notificacion = new Notificacion();
        notificacion.setDato(mensaje);
        notificacion.setRemitente(remitente);
        notificacion.setTicket(Ticket);
        notificacion.setNotificarCliente(true);
        notificacion.setAutogenerada(remitente == null);
        notificacion.setFecha(new Date());
        notificacion.setUrlArchivo("No existente");

        notificacionRepository.save(notificacion);
    }
}