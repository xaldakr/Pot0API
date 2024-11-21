package catolica.edu.pot0tickets.repositories;

import catolica.edu.pot0tickets.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import catolica.edu.pot0tickets.models.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Contar tickets que están en estado "CREADO"
    int countByEstado(String estado);

    // Contar tickets que NO están en los estados dados (para tickets en progreso)
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.estado NOT IN :estados")
    int countByEstadoNotIn(@Param("estados") List<String> estados);

    // Encontrar cliente asociado a un ticket
    @Query("SELECT u FROM Usuario u JOIN Ticket t ON u.idUsuario = t.cliente.idUsuario WHERE t.idTicket = :idTicket")
    Usuario findClienteByTicketId(@Param("idTicket") int idTicket);

    @Query("SELECT new map(t.idTicket as Id, t.servicio as Servicio, t.fecha as FechaDate, t.estado as Estado, CONCAT(c.nombre, ' ', c.apellido) as Cliente, CASE WHEN e IS NULL THEN NULL ELSE CONCAT(e.nombre, ' ', e.apellido) END as Empleado, c.email as Correo) FROM Ticket t JOIN Usuario c ON t.cliente.idUsuario = c.idUsuario LEFT JOIN Usuario e ON t.encargado.idUsuario = e.idUsuario ORDER BY t.fecha DESC")
    List<Map<String, Object>> findDashboardTickets();

    @Query("SELECT new map(t.idTicket as Id, t.servicio as Servicio, t.fecha as FechaDate, t.estado as Estado, CONCAT(c.nombre, ' ', c.apellido) as Cliente, CASE WHEN e IS NULL THEN NULL ELSE CONCAT(e.nombre, ' ', e.apellido) END as Empleado, c.email as Correo) FROM Ticket t JOIN Usuario c ON t.cliente.idUsuario = c.idUsuario LEFT JOIN Usuario e ON t.encargado.idUsuario = e.idUsuario ORDER BY t.fecha DESC")
    List<Map<String, Object>> findAllTickets();

    @Query("SELECT new map(t.idTicket as Id, t.servicio as Servicio, t.fecha as FechaDate, t.estado as Estado, CONCAT(c.nombre, ' ', c.apellido) as Cliente, c.email as Correo) FROM Ticket t JOIN Usuario c ON t.cliente.idUsuario = c.idUsuario WHERE t.encargado IS NULL ORDER BY t.fecha DESC")
    List<Map<String, Object>> findUnassignedTickets();

    @Query("SELECT new map(t.idTicket as Id, t.servicio as Servicio, t.fecha as FechaDate, t.estado as Estado, CONCAT(c.nombre, ' ', c.apellido) as Cliente, CASE WHEN e IS NULL THEN NULL ELSE CONCAT(e.nombre, ' ', e.apellido) END as Empleado, c.email as Correo) FROM Ticket t JOIN Usuario c ON t.cliente.idUsuario = c.idUsuario LEFT JOIN Usuario e ON t.encargado.idUsuario = e.idUsuario WHERE c.idUsuario = :clientId ORDER BY t.fecha DESC")
    List<Map<String, Object>> findTicketsByClient(@Param("clientId") int clientId);

    @Query("SELECT new map(t.idTicket as Id, t.servicio as Servicio, t.fecha as FechaDate, t.estado as Estado, CONCAT(c.nombre, ' ', c.apellido) as Cliente, CASE WHEN e IS NULL THEN NULL ELSE CONCAT(e.nombre, ' ', e.apellido) END as Empleado, c.email as Correo) FROM Ticket t JOIN Usuario c ON t.cliente.idUsuario = c.idUsuario LEFT JOIN Usuario e ON t.encargado.idUsuario = e.idUsuario WHERE t.estado != 'RESUELTO' AND CASE WHEN :filterId = 1 THEN t.servicio IS NOT NULL WHEN :filterId = 2 THEN t.fecha IS NOT NULL WHEN :filterId = 3 THEN e IS NOT NULL WHEN :filterId = 4 THEN c IS NOT NULL END = true ORDER BY t.fecha DESC")
    List<Map<String, Object>> findTicketsByFilter(@Param("filterId") int filterId);

    @Query("SELECT new map(t.idTicket as Id, t.servicio as Servicio, t.fecha as FechaDate, t.estado as Estado, CONCAT(c.nombre, ' ', c.apellido) as Cliente, CASE WHEN e IS NULL THEN NULL ELSE CONCAT(e.nombre, ' ', e.apellido) END as Empleado, c.email as Correo) FROM Ticket t JOIN Usuario c ON t.cliente.idUsuario = c.idUsuario LEFT JOIN Usuario e ON t.encargado.idUsuario = e.idUsuario WHERE t.encargado.idUsuario = :assigneeId AND (:type IS NULL OR (:type = 1 AND NOT t.estado = 'RESUELTO') OR (:type = 2 AND t.estado = 'RESUELTO')) ORDER BY t.fecha DESC")
    List<Map<String, Object>> findTicketsByAssignee(@Param("assigneeId") int assigneeId, @Param("type") Integer type);

    @Query("SELECT new map(t.idTicket as Id, t.servicio as Servicio, t.fecha as FechaDate, t.estado as Estado, " +
       "CONCAT(c.nombre, ' ', c.apellido) as Cliente, " +
       "CASE WHEN e IS NULL THEN NULL ELSE CONCAT(e.nombre, ' ', e.apellido) END as Empleado, " +
       "c.email as Correo) " +
       "FROM Ticket t " +
       "JOIN Usuario c ON t.cliente.idUsuario = c.idUsuario " +
       "LEFT JOIN Usuario e ON t.encargado.idUsuario = e.idUsuario " +
       "WHERE t.fecha >= :startDate AND t.fecha <= :endDate AND " +
       "(:type = 1 AND t.estado = 'RESUELTO' OR " +
       ":type = 2 AND NOT t.estado IN ('RESUELTO', 'CREADO') OR " +
       ":type = 3 AND t.estado = 'CREADO') " +
       "ORDER BY t.fecha DESC")
List<Map<String, Object>> findTicketsByState(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate, 
                                             @Param("type") int type);

    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.archivos a LEFT JOIN FETCH t.tareas ta WHERE t.idTicket = :ticketId")
    Ticket findByIdWithDetails(@Param("ticketId") int ticketId);

    @Query("SELECT new map(t.idTicket as idTicket, t.estado as estado, t.descripcion as descripcion, t.prioridad as prioridad, t.servicio as servicio, t.fecha as fecha, CONCAT(c.nombre, ' ', c.apellido) as Cliente, CASE WHEN e IS NULL THEN NULL ELSE CONCAT(e.nombre, ' ', e.apellido) END as encargado) FROM Ticket t JOIN Usuario c ON t.cliente.idUsuario = c.idUsuario LEFT JOIN Usuario e ON t.encargado.idUsuario = e.idUsuario WHERE t.idTicket = :idTicket")
    Map<String, Object> findTicketDetailById(int idTicket);

    // Obtener detalle completo del ticket
    @Query(value = """
        SELECT 
            t.id_ticket AS idTicket,
            c.nombre AS clienteNombre,
            c.apellido AS clienteApellido,
            c.telefono AS clienteTelefono,
            t.servicio AS servicio,
            t.estado AS estado,
            t.prioridad AS prioridad,
            t.descripcion AS descripcion,
            TO_CHAR(t.fecha, 'DD/MMMM/YYYY') AS fecha,
            CASE 
                WHEN e.id_usuario IS NULL THEN NULL 
                ELSE CONCAT(e.nombre, ' ', e.apellido) 
            END AS encargado,
            (SELECT json_agg(a.url) 
             FROM archivos a 
             WHERE a.id_ticket = t.id_ticket)::json AS archivos,
            (SELECT json_agg(json_build_object(
                'idTarea', tr.id_tarea,
                'nombre', tr.nombre,
                'prioridad', tr.prioridad,
                'info', tr.info,
                'estado', tr.estado,
                'encargado', CASE 
                    WHEN tr.id_encargado IS NULL THEN NULL
                    ELSE CONCAT(u.nombre, ' ', u.apellido)
                END)) 
             FROM tareas tr
             LEFT JOIN usuarios u ON u.id_usuario = tr.id_encargado
             WHERE tr.id_ticket = t.id_ticket)::json AS tareas,
            (SELECT json_agg(json_build_object(
                'remitente', 
                CASE 
                    WHEN n.remitente IS NULL 
                        THEN (CASE WHEN n.autogenerada THEN 'Autogenerada' ELSE 'Anonimo' END) 
                        ELSE CONCAT(usr.nombre, ' ', usr.apellido) 
                END,
                'dato', n.dato,
                'urlArchivo', CASE 
                    WHEN n.url_archivo = 'No existente' THEN NULL 
                    ELSE n.url_archivo 
                END,
                'fecha', TO_CHAR(n.fecha, 'DD/MMMM/YYYY')
            )) 
            FROM notificaciones n
            LEFT JOIN usuarios usr ON usr.id_usuario = n.remitente
            WHERE n.id_ticket = t.id_ticket
            )::json AS notificaciones
        FROM tickets t
        JOIN usuarios c ON c.id_usuario = t.id_cliente
        LEFT JOIN usuarios e ON e.id_usuario = t.id_encargado
        WHERE t.id_ticket = :idTicket
    """, nativeQuery = true)
    Map<String, Object> findTicketDetail(@Param("idTicket") int idTicket);

    // Obtener detalle del ticket visible para clientes (filtrando notificaciones)
    @Query(value = """
        SELECT 
            t.id_ticket AS idTicket,
            c.nombre AS clienteNombre,
            c.apellido AS clienteApellido,
            c.telefono AS clienteTelefono,
            t.servicio AS servicio,
            t.estado AS estado,
            t.prioridad AS prioridad,
            t.descripcion AS descripcion,
            TO_CHAR(t.fecha, 'DD/MMMM/YYYY') AS fecha,
            CASE 
                WHEN e.id_usuario IS NULL THEN NULL 
                ELSE CONCAT(e.nombre, ' ', e.apellido) 
            END AS encargado,
            (SELECT json_agg(a.url) 
             FROM archivos a 
             WHERE a.id_ticket = t.id_ticket)::json AS archivos,
            (SELECT json_agg(json_build_object(
                'idTarea', tr.id_tarea,
                'nombre', tr.nombre,
                'prioridad', tr.prioridad,
                'info', tr.info,
                'estado', tr.estado,
                'encargado', CASE 
                    WHEN tr.id_encargado IS NULL THEN NULL
                    ELSE CONCAT(u.nombre, ' ', u.apellido)
                END)) 
             FROM tareas tr
             LEFT JOIN usuarios u ON u.id_usuario = tr.id_encargado
             WHERE tr.id_ticket = t.id_ticket)::json AS tareas,
            (SELECT json_agg(json_build_object(
                'remitente', 
                CASE 
                    WHEN n.remitente IS NULL 
                        THEN (CASE WHEN n.autogenerada THEN 'Autogenerada' ELSE 'Anonimo' END) 
                        ELSE CONCAT(usr.nombre, ' ', usr.apellido) 
                END,
                'dato', n.dato,
                'urlArchivo', CASE 
                    WHEN n.url_archivo = 'No existente' THEN NULL 
                    ELSE n.url_archivo 
                END,
                'fecha', TO_CHAR(n.fecha, 'DD/MMMM/YYYY')
            )) 
            FROM notificaciones n
            LEFT JOIN usuarios usr ON usr.id_usuario = n.remitente
            WHERE n.id_ticket = t.id_ticket AND n.notificar_cliente = true
            )::json AS notificaciones
        FROM tickets t
        JOIN usuarios c ON c.id_usuario = t.id_cliente
        LEFT JOIN usuarios e ON e.id_usuario = t.id_encargado
        WHERE t.id_ticket = :idTicket
    """, nativeQuery = true)
    Map<String, Object> findTicketDetailForClient(@Param("idTicket") int idTicket);


                                                 
}

