package catolica.edu.pot0tickets.controllers;
import catolica.edu.pot0tickets.models.Notificacion;
import catolica.edu.pot0tickets.models.Rol;
import catolica.edu.pot0tickets.models.Tarea;
import catolica.edu.pot0tickets.models.Ticket;
import catolica.edu.pot0tickets.models.Usuario;
import catolica.edu.pot0tickets.repositories.*;
import catolica.edu.pot0tickets.services.CorreoService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/misc")
public class MiscController {

    @Autowired
    private final TicketRepository ticketRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final TareaRepository tareaRepository;
    @Autowired
    private final NotificacionRepository notificacionRepository;
    @Autowired
    private final RolRepository rolRepository;
    @Autowired
    private final CorreoService correoService;

    public MiscController(TicketRepository ticketRepository, UsuarioRepository usuarioRepository, TareaRepository tareaRepository, 
                          NotificacionRepository notificacionRepository, RolRepository rolRepository, CorreoService correoService) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.tareaRepository = tareaRepository;
        this.notificacionRepository = notificacionRepository;
        this.rolRepository = rolRepository;
        this.correoService = correoService;
    }

    @GetMapping("/ObtenerEstados")
    public ResponseEntity<String[]> getEstados() {
        String[] estados = {"EN ESPERA DE INFORMACIÓN", "EN RESOLUCIÓN", "RESUELTO"};
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/ObtenerRoles")
    public ResponseEntity<List<Rol>> getRoles() {
        return ResponseEntity.ok(rolRepository.findAll());
    }

    @GetMapping("/ObtenerPrioridades")
    public ResponseEntity<String[]> getPrioridades() {
        String[] prioridades = {"BAJA", "NORMAL", "IMPORTANTE", "CRÍTICA"};
        return ResponseEntity.ok(prioridades);
    }

    @GetMapping("/ObtenerTiposTicket")
    public ResponseEntity<Map<String, Integer>> getTiposTicket() {
        int noAbiertos = ticketRepository.countByEstado("CREADO");
        int noEnProgreso = ticketRepository.countByEstadoNotIn(List.of("RESUELTO", "CREADO"));
        int noCerrados = ticketRepository.countByEstado("RESUELTO");

        return ResponseEntity.ok(Map.of(
            "noabiertos", noAbiertos,
            "noprogreso", noEnProgreso,
            "nocerrados", noCerrados
        ));
    }

    @GetMapping("/ObtenerTareas/{id}/{tipo}")
    public ResponseEntity<?> obtenerTareas(@PathVariable int id, @RequestParam(defaultValue = "-1") int idticket,
                                           @RequestParam(defaultValue = "0") int tipo, @RequestParam(defaultValue = "") String nombre) {
        List<Tarea> tareas = tareaRepository.findAllByEncargadoAndNombreContains(id, nombre)
                                            .stream()
                                            .filter(t -> (idticket == -1 || t.getIdTicket() == idticket) &&
                                                         (tipo == 0 || (tipo == 1 && !t.isCompletada()) || (tipo == 2 && t.isCompletada())))
                                            .collect(Collectors.toList());

        if (tareas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tareas);
    }

    @PostMapping("/CrearTarea")
    public ResponseEntity<?> crearTarea(@RequestBody Map<String, Object> tareaData) {
        String nombre = (String) tareaData.get("nombre");
        String info = (String) tareaData.get("info");
        String prioridad = (String) tareaData.get("prioridad");
        int idTicket = (Integer) tareaData.get("id_ticket");
        int idEncargado = (Integer) tareaData.get("id_encargado");

        // Validaciones de prioridad y existencia de ticket y encargado
        if (!List.of("BAJA", "NORMAL", "IMPORTANTE", "CRÍTICA").contains(prioridad)) {
            return ResponseEntity.badRequest().body("Prioridad no válida.");
        }
        
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
        Usuario encargado = usuarioRepository.findById(idEncargado).orElse(null);
        if (ticket == null) return ResponseEntity.notFound().build();
        if (encargado == null || rolRepository.findByTipoRolAndIdRolNot(1, encargado.getIdRol()) == null) {
            return ResponseEntity.badRequest().body("Encargado inválido.");
        }

        Tarea nuevaTarea = new Tarea(nombre, info, prioridad, "", false, idTicket, idEncargado);
        tareaRepository.save(nuevaTarea);

        // Notificación
        Notificacion notificacion = new Notificacion(
                "Se ha creado y asignado la tarea " + nombre + " al soporte " + encargado.getNombreCompleto(), true, true,
                null, idTicket);
        notificacionRepository.save(notificacion);

        // Enviar correos
        correoService.enviarAsignacionTareaCorreo(encargado.getEmail(), nuevaTarea.getNombre(), nuevaTarea.getNombre(), ticket.getServicio());
        Usuario cliente = usuarioRepository.findClienteByTicketId(idTicket);
        correoService.enviarComentarioTicket(cliente.getEmail(), idTicket, "Sistema de Notificaciones Autogeneradas", ticket.getServicio(), notificacion.getDato(), "");

        return ResponseEntity.ok("Tarea creada exitosamente.");
    }

    @PatchMapping("/RechazarTarea/{id_tarea}")
    public ResponseEntity<String> rechazarTarea(@PathVariable int id_tarea) {
        Tarea tarea = tareaRepository.findById(id_tarea).orElse(null);
        if (tarea == null || tarea.isCompletada()) {
            return ResponseEntity.badRequest().body("Tarea no válida o ya completada.");
        }

        tarea.setIdEncargado(null);
        tareaRepository.save(tarea);

        Notificacion notificacion = new Notificacion("El soporte ha rechazado la tarea " + tarea.getNombre(), "No existente", false, tarea.getIdTicket());
        notificacionRepository.save(notificacion);

        Ticket ticket = ticketRepository.findById(tarea.getIdTicket()).orElse(null);
        Usuario duenotarea = usuarioRepository.findById(ticket.getIdEncargado()).orElse(null);
        correoService.enviarRechazoTarea(duenotarea.getEmail(), tarea.getNombre(), tarea.getIdTicket(), ticket.getServicio());

        return ResponseEntity.ok("Encargado de la tarea eliminado exitosamente.");
    }

    @PatchMapping("/AsignarTarea/{id_tarea}")
    public ResponseEntity<?> asignarTarea(@PathVariable int id_tarea, @RequestBody Map<String, Object> asignacionData) {
        int idEncargado = (Integer) asignacionData.get("id_encargado");
        Usuario usuario = usuarioRepository.findById(idEncargado).orElse(null);

        if (usuario == null || rolRepository.findByTipoRolAndIdRolNot(1, usuario.getIdRol()) == null) {
            return ResponseEntity.badRequest().body("Encargado no válido.");
        }

        Tarea tarea = tareaRepository.findById(id_tarea).orElse(null);
        if (tarea == null || tarea.isCompletada()) {
            return ResponseEntity.badRequest().body("Tarea no válida o ya completada.");
        }

        tarea.setIdEncargado(idEncargado);
        tareaRepository.save(tarea);

        Notificacion notificacion = new Notificacion("Tarea asignada al soporte " + usuario.getNombreCompleto(), "No existente", false, tarea.getIdTicket());
        notificacionRepository.save(notificacion);

        Ticket ticket = ticketRepository.findById(tarea.getIdTicket()).orElse(null);
        correoService.enviarAsignacionTarea(usuario.getEmail(), tarea, ticket.getServicio());

        return ResponseEntity.ok("Encargado asignado a la tarea exitosamente.");
    }

    @PatchMapping("/EditarTarea/{id_tarea}")
    public ResponseEntity<?> editarTarea(@PathVariable int id_tarea, @RequestBody Map<String, Object> datedit) {
        boolean completada = (Boolean) datedit.get("completada");
        String estado = (String) datedit.get("estado");

        Tarea tarea = tareaRepository.findById(id_tarea).orElse(null);
        if (tarea == null || tarea.isCompletada()) {
            return ResponseEntity.badRequest().body("Tarea no válida o ya finalizada.");
        }

        tarea.setEstado(estado);
        tarea.setCompletada(completada);
        tareaRepository.save(tarea);

        Ticket ticket = ticketRepository.findById(tarea.getIdTicket()).orElse(null);
        Usuario encargado = usuarioRepository.findById(ticket.getIdEncargado()).orElse(null);
        Usuario cliente = usuarioRepository.findById(ticket.getIdCliente()).orElse(null);
        Usuario ejecutor = usuarioRepository.findById(tarea.getIdEncargado()).orElse(null);

        correoService.enviarCambioEstadoTarea(encargado.getEmail(), ticket.getIdTicket(), ejecutor.getNombreCompleto(), tarea.getInfo(), ticket.getServicio(), estado, completada);

        if (completada) {
            correoService.enviarCambioEstadoTarea(cliente.getEmail(), ticket.getIdTicket(), ejecutor.getNombreCompleto(), tarea.getInfo(), ticket.getServicio(), estado, completada);
        }
        return ResponseEntity.ok(tarea);
    }
}
