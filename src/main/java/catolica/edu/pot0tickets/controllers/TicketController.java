package catolica.edu.pot0tickets.controllers;

import catolica.edu.pot0tickets.models.Ticket;
import catolica.edu.pot0tickets.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/ObtenerDash")
    public ResponseEntity<?> getDashboardTickets() {
        List<Map<String, Object>> tickets = ticketService.getDashboardTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerTodo")
    public ResponseEntity<?> getAllTickets() {
        List<Map<String, Object>> tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerSinAsignar")
    public ResponseEntity<?> getUnassignedTickets() {
        List<Map<String, Object>> tickets = ticketService.getUnassignedTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerPorCliente/{id}")
    public ResponseEntity<?> getTicketsByClient(@PathVariable int id) {
        List<Map<String, Object>> tickets = ticketService.getTicketsByClient(id);
        if (tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerPorFiltro/{id}")
    public ResponseEntity<?> getTicketsByFilter(@PathVariable int id) {
        List<Map<String, Object>> tickets = ticketService.getTicketsByFilter(id);
        if (tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerPorEncargado/{id}/{tipo}")
    public ResponseEntity<?> getTicketsByAssignee(@PathVariable int id, @PathVariable(required = false) Integer tipo) {
        List<Map<String, Object>> tickets = ticketService.getTicketsByAssignee(id, tipo);
        if (tickets.isEmpty()) {
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/ObtenerPorEstado")
    public ResponseEntity<?> getTicketsByState(@RequestBody Map<String, Object> data) {
        // Parsear o establecer valores predeterminados para las fechas
        OffsetDateTime startDateTime = data.containsKey("inicio")
                ? OffsetDateTime.parse(data.get("inicio").toString())
                : OffsetDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0).withNano(0);

        OffsetDateTime endDateTime = data.containsKey("fin")
                ? OffsetDateTime.parse(data.get("fin").toString())
                : OffsetDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // Convertir las fechas a LocalDate para pasarlas al servicio
        LocalDateTime startDate = startDateTime.toLocalDateTime();
        LocalDateTime endDate = endDateTime.toLocalDateTime();

        // Parsear el tipo desde el mapa de datos
        int type = Integer.parseInt(data.get("tipo").toString());

        // Llamar al servicio
        List<Map<String, Object>> tickets = ticketService.getTicketsByState(startDate, endDate, type);

        // Responder al cliente
        return ResponseEntity.ok(tickets);
    }
    @GetMapping("/ObtenerDetalle/{id}")
    public ResponseEntity<?> getTicketDetail(@PathVariable("id") int idTicket) {
        Map<String, Object> ticketDetail = ticketService.getTicketDetail(idTicket);
        return ResponseEntity.ok(ticketDetail);
    }



    @PostMapping("/CrearTicket")
    public ResponseEntity<Ticket> createTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("servicio") String servicio,
            @RequestParam("prioridad") String prioridad,
            @RequestParam("id_cliente") int idCliente,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        try {
            
            Ticket createdTicket = ticketService.createTicket(descripcion, servicio, prioridad, idCliente, archivo);
            return ResponseEntity.ok(createdTicket);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/ObtenerDetalleCliente/{id}")
    public ResponseEntity<?> getTicketDetailForClient(@PathVariable("id") int idTicket) {
        Map<String, Object> ticketDetail = ticketService.getTicketDetailForClient(idTicket);
        return ResponseEntity.ok(ticketDetail);
    }

    @PatchMapping("/EditarTicket/{id}")
    public ResponseEntity<Ticket> updateTicketState(
            @PathVariable("id") int ticketId,
            @RequestParam("estado") String nuevoEstado,
            @RequestParam("idEjecutor") int idEjecutor) {
        try {
            Ticket updatedTicket = ticketService.updateTicketState(ticketId, nuevoEstado, idEjecutor);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/AsignarSoporte/{id}")
    public ResponseEntity<Ticket> assignSupport(
            @PathVariable("id") int ticketId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Extraer el idSoporte del Map
            int idSoporte = (int) requestBody.get("idSoporte");
            Ticket updatedTicket = ticketService.assignSupport(ticketId, idSoporte);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}