package catolica.edu.pot0tickets.services;

import catolica.edu.pot0tickets.models.Usuario;
import catolica.edu.pot0tickets.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CorreoService correoService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, CorreoService correoService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.correoService = correoService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Object> getDashboardInfo() {
        return usuarioRepository.getDashboardInfo();
    }

    public List<Object> findUsersByRoleAndEmail(int tipo, String busqueda) {
        return usuarioRepository.findUsersByRoleAndEmail(tipo, busqueda);
    }

    public List<Object> findTechnicians(String busqueda) {
        return usuarioRepository.findTechnicians(busqueda);
    }

    public Usuario logIn(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null && passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            return usuario;
        }
        return null;
    }

    public Usuario createUsuario(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena())); // Encriptar la contraseña
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        correoService.enviarBienvenida(nuevoUsuario);
        return nuevoUsuario;
    }

    public Usuario updateUsuario(int id, Usuario updatedUsuario) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(updatedUsuario.getNombre());
            usuario.setApellido(updatedUsuario.getApellido());
            usuario.setTelefono(updatedUsuario.getTelefono());
            usuario.setEmail(updatedUsuario.getEmail());
            usuario.setContrasena(passwordEncoder.encode(updatedUsuario.getContrasena()));
            usuario.setTelContacto(updatedUsuario.getTelContacto());
            usuario.setIdRol(updatedUsuario.getIdRol());
            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            correoService.enviarCambioDatos(usuarioActualizado);
            return usuarioActualizado;
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
