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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
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
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerTodo")
    public ResponseEntity<?> getAllTickets() {
        List<Map<String, Object>> tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerSinAsignar")
    public ResponseEntity<?> getUnassignedTickets() {
        List<Map<String, Object>> tickets = ticketService.getUnassignedTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerPorCliente/{id}")
    public ResponseEntity<?> getTicketsByClient(@PathVariable int id) {
        List<Map<String, Object>> tickets = ticketService.getTicketsByClient(id);
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerPorFiltro/{id}")
    public ResponseEntity<?> getTicketsByFilter(@PathVariable int id) {
        List<Map<String, Object>> tickets = ticketService.getTicketsByFilter(id);
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ObtenerPorEncargado/{id}/{tipo}")
    public ResponseEntity<?> getTicketsByAssignee(@PathVariable int id, @PathVariable(required = false) Integer tipo) {
        List<Map<String, Object>> tickets = ticketService.getTicketsByAssignee(id, tipo);
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/ObtenerPorEstado")
    public ResponseEntity<?> getTicketsByState(@RequestBody Map<String, Object> data) {
        LocalDate startDate = LocalDate.parse(data.get("inicio").toString());
        LocalDate endDate = LocalDate.parse(data.get("fin").toString());
        int type = Integer.parseInt(data.get("tipo").toString());

        List<Map<String, Object>> tickets = ticketService.getTicketsByState(startDate, endDate, type);
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }
    @GetMapping("/ObtenerDetalle/{id}")
    public ResponseEntity<Map<String, Object>> getTicketDetail(@PathVariable("id") int idTicket) {
        Map<String, Object> ticketDetail = ticketService.getTicketDetail(idTicket);
        if (ticketDetail == null) {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<Map<String, Object>> getTicketDetailForClient(@PathVariable("id") int idTicket) {
        Map<String, Object> ticketDetail = ticketService.getTicketDetailForClient(idTicket);
        if (ticketDetail == null) {
            return ResponseEntity.notFound().build();
        }
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
            @RequestParam("idSoporte") int idSoporte) {
        try {
            Ticket updatedTicket = ticketService.assignSupport(ticketId, idSoporte);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}