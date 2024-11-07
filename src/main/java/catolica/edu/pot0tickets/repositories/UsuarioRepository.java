package catolica.edu.pot0tickets.repositories;

import catolica.edu.pot0tickets.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Usuario findByEmail(@Param("email") String email);

    @Query("SELECT new map(u.idUsuario AS id_usuario, CONCAT(u.nombre, ' ', u.apellido) AS nombre, u.email AS email, u.telefono AS telefono, COUNT(t) AS no_tickets) " +
           "FROM Usuario u " +
           "JOIN u.rol r " +
           "JOIN r.tipoRol tr " +
           "LEFT JOIN u.tickets t " +
           "WHERE tr.idTipoRol != 1 " +
           "GROUP BY u.idUsuario ORDER BY no_tickets DESC")
    List<Object> getDashboardInfo();

    @Query("SELECT new map(u.idUsuario AS id_usuario, CONCAT(u.nombre, ' ', u.apellido) AS nombre, u.nombre AS nom, u.apellido AS ape, u.telContacto AS contacto, u.email AS email, u.telefono AS telefono, u.idRol AS id_rol) " +
           "FROM Usuario u " +
           "WHERE u.idRol = :tipo AND u.email LIKE %:busqueda% " +
           "ORDER BY u.nombre DESC")
    List<Object> findUsersByRoleAndEmail(@Param("tipo") int tipo, @Param("busqueda") String busqueda);

    @Query("SELECT new map(u.idUsuario AS id_usuario, CONCAT(u.nombre, ' ', u.apellido) AS nombre, u.nombre AS nom, u.apellido AS ape, u.telContacto AS contacto, u.email AS email, u.telefono AS telefono) " +
           "FROM Usuario u " +
           "JOIN u.rol r " +
           "WHERE r.tipoRol = 2 AND u.email LIKE %:busqueda% " +
           "ORDER BY u.nombre DESC")
    List<Object> findTechnicians(@Param("busqueda") String busqueda);
}
