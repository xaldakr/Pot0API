package catolica.edu.pot0tickets.repositories;

import catolica.edu.pot0tickets.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    // Encontrar roles por tipo específico (evitando el tipo "Cliente")
    @Query("SELECT r FROM Rol r WHERE r.tipoRol.idTipoRol != :tipoCliente AND r.idRol = :idrolencargado")
    List<Rol> findByTipoRolAndIdRolNot(@Param("tipoCliente") int tipoCliente, @Param("idrolencargado") int idrolencargado);
    
}

