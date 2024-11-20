package catolica.edu.pot0tickets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/api/**" // Asegúrate de que se permite el acceso a cualquier endpoint en "/api/**"
                                ).permitAll()
                                .anyRequest().permitAll()  // Las demás rutas requieren autenticación
                )
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF si no se necesita
                .formLogin(login -> login.disable()) // Desactivar login por formulario si no lo necesitas
                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Permite que el contenido de Swagger se muestre en un iframe
                        .xssProtection().disable() // Desactiva la protección XSS (solo en desarrollo)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
