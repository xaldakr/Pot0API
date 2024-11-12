package catolica.edu.pot0tickets.repositories;

import catolica.edu.pot0tickets.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import catolica.edu.pot0tickets.models.Usuario;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Contar tickets que están en estado "CREADO"
    int countByEstado(String estado);

    // Contar tickets que NO están en los estados dados (para tickets en progreso)
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.estado NOT IN :estados")
    int countByEstadoNotIn(@Param("estados") List<String> estados);

    // Encontrar cliente asociado a un ticket
    @Query("SELECT u FROM Usuario u JOIN Ticket t ON u.idUsuario = t.idCliente WHERE t.idTicket = :idTicket")
    Usuario findClienteByTicketId(@Param("idTicket") int idTicket);
}

