package catolica.edu.pot0tickets.services;

import catolica.edu.pot0tickets.models.Archivo;
import catolica.edu.pot0tickets.models.Ticket;
import catolica.edu.pot0tickets.models.Usuario;
import catolica.edu.pot0tickets.repositories.TicketRepository;
import catolica.edu.pot0tickets.repositories.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private CorreoService correoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private ArchivoService archivoService;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Map<String, Object>> getDashboardTickets() {
        return ticketRepository.findDashboardTickets();
    }

    public List<Map<String, Object>> getAllTickets() {
        return ticketRepository.findAllTickets();
    }

    public List<Map<String, Object>> getUnassignedTickets() {
        return ticketRepository.findUnassignedTickets();
    }

    public List<Map<String, Object>> getTicketsByClient(int clientId) {
        return ticketRepository.findTicketsByClient(clientId);
    }

    public List<Map<String, Object>> getTicketsByFilter(int filterId) {
        return ticketRepository.findTicketsByFilter(filterId);
    }

    public List<Map<String, Object>> getTicketsByAssignee(int assigneeId, Integer type) {
        return ticketRepository.findTicketsByAssignee(assigneeId, type);
    }

    public List<Map<String, Object>> getTicketsByState(LocalDateTime startDate, LocalDateTime endDate, int type) {
        return ticketRepository.findTicketsByState(startDate, endDate, type);
    }

    public Map<String, Object> getTicketDetail(int idTicket) {
        return ticketRepository.findTicketDetail(idTicket);
    }

    public Ticket createTicket(String descripcion, String servicio, String prioridad, int idCliente, MultipartFile file) throws IOException {
        Usuario Cliente = usuarioRepository.findById(idCliente).orElseThrow(() -> new RuntimeException("Usuario no encontrado."));;
        Ticket ticket = new Ticket();
        ticket.setEstado("CREADO");
        ticket.setDescripcion(descripcion);
        ticket.setPrioridad(prioridad);
        ticket.setServicio(servicio);
        ticket.setFecha(new Date());
        ticket.setCliente(Cliente);

        // Guardar ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // Subir archivo si existe
        if (file != null && !file.isEmpty()) {
            String fileUrl = firebaseStorageService.uploadFile(file);
            Archivo archivo = new Archivo();
            archivo.setUrl(fileUrl);
            archivo.setTicket(savedTicket);
            archivoService.saveArchivo(archivo);
        }

        // Enviar correo de confirmación
        correoService.enviarTicketCorreo(ticket.getCliente().getEmail(), savedTicket.getIdTicket(), servicio, descripcion);

        return savedTicket;
    }
    public Map<String, Object> getTicketDetailForClient(int idTicket) {
        return ticketRepository.findTicketDetailForClient(idTicket);
    }

    @Transactional
    public Ticket updateTicketState(int ticketId, String nuevoEstado, int idEjecutor) {
        // Obtener ticket
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado."));

        // Validar estado
        List<String> estadosValidos = List.of("CREADO", "ASIGNADO", "EN ESPERA DE INFORMACIÓN", "EN RESOLUCIÓN", "RESUELTO");
        if (!estadosValidos.contains(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no válido.");
        }

        // Validar usuario ejecutor
        Usuario ejecutor = usuarioRepository.findById(idEjecutor).orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        boolean esEncargado = ticket.getEncargado() != null && ticket.getEncargado().getIdUsuario() == idEjecutor;
        boolean esAdministrador = ejecutor.getRol().getIdRol() == 3;

        if (!esEncargado && !esAdministrador) {
            throw new SecurityException("No tienes permisos para editar este ticket.");
        }

        // Validar si el ticket ya está resuelto
        if ("RESUELTO".equals(ticket.getEstado())) {
            throw new IllegalArgumentException("El ticket ya está cerrado.");
        }

        // Verificar tareas antes de resolver
        if ("RESUELTO".equals(nuevoEstado) && ticket.hasIncompleteTasks()) {
            throw new IllegalArgumentException("No se puede resolver el ticket porque hay tareas incompletas.");
        }

        // Actualizar estado
        ticket.setEstado(nuevoEstado);
        ticket.setResuelta("RESUELTO".equals(nuevoEstado));
        ticketRepository.save(ticket);

        // Crear notificación
        String mensaje = "RESUELTO".equals(nuevoEstado) ? "El ticket se ha cerrado" : "Estado del ticket cambiado a " + nuevoEstado;
        notificacionService.createNotificacion(mensaje, ejecutor, ticket);

        // Enviar correos
        correoService.enviarCambioEstadoTicketCorreo(
                ticket.getCliente().getEmail(), ticket.getIdTicket(), ticket.getServicio(), nuevoEstado);

        return ticket;
    }

    @Transactional
    public Ticket assignSupport(int ticketId, int idSoporte) {
        // Verificar que el usuario sea soporte
        Usuario soporte = usuarioRepository.findByIdAndNoRole(idSoporte, 1)
                .orElseThrow(() -> new RuntimeException("Usuario de soporte no encontrado."));

        // Obtener el ticket
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado."));

        // Validar si el ticket ya tiene un encargado
        if (ticket.getEncargado() != null) {
            throw new IllegalArgumentException("El ticket ya tiene un encargado asignado.");
        }

        // Asignar soporte y actualizar estado
        ticket.setEncargado(soporte);
        ticket.setEstado("ASIGNADO");
        ticketRepository.save(ticket);

        // Crear notificación
        String mensaje = "Se ha asignado el ticket al soporte " + soporte.getNombreCompleto();
        notificacionService.createNotificacion(mensaje, null, ticket);

        // Enviar correos
        correoService.enviarAsignacionTicketCorreo(
                soporte.getEmail(), ticket.getIdTicket(), ticket.getServicio(), ticket.getPrioridad());

        return ticket;
    }
}
