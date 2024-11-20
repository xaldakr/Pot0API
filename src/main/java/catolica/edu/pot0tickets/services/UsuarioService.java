package catolica.edu.pot0tickets.services;

import catolica.edu.pot0tickets.models.Rol;
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
        String contra = usuario.getContrasena();
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena())); // Encriptar la contraseña
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        correoService.enviarBienvenida(nuevoUsuario, contra);
        return nuevoUsuario;
    }

    public Usuario updateUsuario(
        int id,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String contrasena,
        String telContacto,
        Rol rol) {
    return usuarioRepository.findById(id).map(usuario -> {
        if (nombre != null) {
            usuario.setNombre(nombre);
        }
        if (apellido != null) {
            usuario.setApellido(apellido);
        }
        if (telefono != null) {
            usuario.setTelefono(telefono);
        }
        if (email != null) {
            usuario.setEmail(email);
        }
        if (contrasena != null) {
            usuario.setContrasena(passwordEncoder.encode(contrasena));
        }
        if (telContacto != null) {
            usuario.setTelContacto(telContacto);
        }
        if (rol != null) {
            usuario.setRol(rol);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

            // Enviar correo si la contraseña fue actualizada
            if (contrasena != null) {
                correoService.enviarCambioDatos(usuarioActualizado, contrasena);
            } else {
                correoService.enviarCambioDatos(usuarioActualizado, " la misma que la vez anterior, revise el correo anterior de creación o cambio de credenciales");
            }
    
            return usuarioActualizado;
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
}
