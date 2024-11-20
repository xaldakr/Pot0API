package catolica.edu.pot0tickets.controllers;

import catolica.edu.pot0tickets.models.Rol;
import catolica.edu.pot0tickets.models.Usuario;
import catolica.edu.pot0tickets.services.UsuarioService;
import catolica.edu.pot0tickets.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    @Autowired
    public UsuarioController(UsuarioService usuarioService, RolRepository rolRepository) {
        this.usuarioService = usuarioService;
        this.rolRepository=rolRepository;
    }

    @GetMapping("/ObtenerDash")
    public ResponseEntity<List<Object>> getDashboardInfo() {
        List<Object> result = usuarioService.getDashboardInfo();
        return result.isEmpty() ? ResponseEntity.ok(result) : ResponseEntity.ok(result);
    }

    @GetMapping("/ObtenerUsus/{tipo}")
    public ResponseEntity<List<Object>> getUsersByRole(@PathVariable int tipo, @RequestParam(defaultValue = "") String busqueda) {
        List<Object> usuarios = usuarioService.findUsersByRoleAndEmail(tipo, busqueda);
        return usuarios.isEmpty() ? ResponseEntity.ok(usuarios) : ResponseEntity.ok(usuarios);
    }

    @GetMapping("/ObtenerTecnicos")
    public ResponseEntity<List<Object>> getTechnicians(@RequestParam(defaultValue = "") String busqueda) {
        List<Object> tecnicos = usuarioService.findTechnicians(busqueda);
        return tecnicos.isEmpty() ? ResponseEntity.ok(tecnicos) : ResponseEntity.ok(tecnicos);
    }

    @PostMapping("/Login")
    public ResponseEntity<Usuario> logIn(@RequestBody Map<String, String> data) {
        String email = data.get("correo");
        String contrasena = data.get("contrasena");
        Usuario usuario = usuarioService.logIn(email, contrasena);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.status(404).body(null);
    }

    @PostMapping("/CrearUsuario")
    public ResponseEntity<Usuario> createUsuario(@RequestBody Map<String, Object> requestBody) {
        // Obtener los datos del Map
        String nombre = (String) requestBody.get("nombre");
        String apellido = (String) requestBody.get("apellido");
        String telefono = (String) requestBody.get("telefono");
        String email = (String) requestBody.get("email");
        String contrasena = (String) requestBody.get("contrasena");
        String telContacto = (String) requestBody.get("telContacto");
        Integer rolId = (Integer) requestBody.get("rolId");  // Usando 'rol' en vez de 'rolId'
    
        // Obtener el Rol desde el repositorio utilizando el rolId
        Rol rol = rolRepository.findById(rolId)
                               .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
    
        // Crear el nuevo Usuario utilizando los datos extraídos del Map
        Usuario nuevoUsuario = new Usuario(
            nombre,
            apellido,
            telefono,
            email,
            contrasena,
            telContacto,
            rol
        );
    
        // Guardar el nuevo Usuario
        Usuario usuarioGuardado = usuarioService.createUsuario(nuevoUsuario);
        
        return ResponseEntity.ok(usuarioGuardado);
    }
    


    @PatchMapping("/EditarUsuario/{id_usuario}")
public ResponseEntity<Usuario> updateUsuario(@PathVariable int id_usuario, @RequestBody Map<String, Object> requestBody) {
    // Extraer datos opcionales del Map
    String nombre = (String) requestBody.get("nombre");
    String apellido = (String) requestBody.get("apellido");
    String telefono = (String) requestBody.get("telefono");
    String email = (String) requestBody.get("email");
    String contrasena = (String) requestBody.get("contrasena");
    String telContacto = (String) requestBody.get("telContacto");
    Integer rolId = (Integer) requestBody.get("rolId");

    // Buscar el Rol si está presente en el cuerpo
    Rol rol = null;
    if (rolId != null) {
        rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
    }

    // Llamar al servicio para realizar la actualización
    Usuario usuarioActualizado = usuarioService.updateUsuario(id_usuario, nombre, apellido, telefono, email, contrasena, telContacto, rol);

    return ResponseEntity.ok(usuarioActualizado);
}

}
