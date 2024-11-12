package catolica.edu.pot0tickets.repositories;
import catolica.edu.pot0tickets.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {


}

