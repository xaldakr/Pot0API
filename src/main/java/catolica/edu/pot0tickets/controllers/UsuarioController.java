package catolica.edu.pot0tickets.controllers;

import catolica.edu.pot0tickets.models.Usuario;
import catolica.edu.pot0tickets.services.UsuarioService;
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

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/ObtenerDash")
    public ResponseEntity<List<Object>> getDashboardInfo() {
        List<Object> result = usuarioService.getDashboardInfo();
        return result.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }

    @GetMapping("/ObtenerUsus/{tipo}")
    public ResponseEntity<List<Object>> getUsersByRole(@PathVariable int tipo, @RequestParam(defaultValue = "") String busqueda) {
        List<Object> usuarios = usuarioService.findUsersByRoleAndEmail(tipo, busqueda);
        return usuarios.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(usuarios);
    }

    @GetMapping("/ObtenerTecnicos")
    public ResponseEntity<List<Object>> getTechnicians(@RequestParam(defaultValue = "") String busqueda) {
        List<Object> tecnicos = usuarioService.findTechnicians(busqueda);
        return tecnicos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(tecnicos);
    }

    @PostMapping("/Login")
    public ResponseEntity<Usuario> logIn(@RequestBody Map<String, String> data) {
        String email = data.get("correo");
        String contrasena = data.get("contrasena");
        Usuario usuario = usuarioService.logIn(email, contrasena);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.status(404).body(null);
    }

    @PostMapping("/CrearUsuario")
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PatchMapping("/EditarUsuario/{id_usuario}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable int id_usuario, @RequestBody Usuario updatedUsuario) {
        Usuario usuario = usuarioService.updateUsuario(id_usuario, updatedUsuario);
        return ResponseEntity.ok(usuario);
    }
}
