package catolica.edu.pot0tickets.repositories;

import catolica.edu.pot0tickets.models.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {

    // Encontrar todas las tareas asignadas a un encargado específico que contienen un nombre determinado
    @Query("SELECT t FROM Tarea t WHERE t.encargado.idUsuario = :idEncargado AND t.nombre LIKE %:nombre%")
    List<Tarea> findAllByEncargadoAndNombreContains(@Param("idEncargado") int idEncargado, @Param("nombre") String nombre);

    // Obtener tareas según su estado de finalización
    List<Tarea> findByCompletada(boolean completada);
}

