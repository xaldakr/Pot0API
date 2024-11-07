package catolica.edu.pot0tickets.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import catolica.edu.pot0tickets.models.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviar(String destinatario, String asunto, String cuerpo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpo, true); // `true` para enviar el cuerpo en HTML

            mailSender.send(mensaje);
            System.out.println("Correo enviado correctamente a " + destinatario);
        } catch (MessagingException e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }

    public void enviarTicketCorreo(String destinatario, int id, String servicio, String descripcion) {
        String asunto = "Ticket Creado con id " + id;
        
        String cuerpo = """
                <html>
                <head>
                    <style>
                        .header {
                            background-color: #2E3B4E;
                            color: white;
                            text-align: center;
                            padding: 10px 0;
                            font-size: 24px;
                            font-weight: bold;
                        }
                        .content {
                            background-color: #D3DCE5;
                            padding: 20px;
                            font-family: Arial, sans-serif;
                            font-size: 18px;
                            text-align: center;
                            color: black;
                        }
                        .highlight {
                            color: #8D774A;
                            font-weight: bold;
                        }
                        .service {
                            color: #8D774A;
                        }
                        .description {
                            color: #8D774A;
                            font-style: italic;
                        }
                    </style>
                </head>
                <body>
                    <div class='header'>Creación de Ticket</div>
                    <div class='content'>
                        <p>Haz creado tu ticket correctamente</p>
                        <p>El id de tu ticket es <span class='highlight'>""" + id + """
                        </span></p>
                        <p>El servicio de revisión es <span class='service'>""" + servicio + """
                        </span> con la descripción de:</p>
                        <p class='description'>""" + descripcion + """
                        </p>
                        <p>Espera más detalles cuando el personal se encargue del ticket</p>
                    </div>
                </body>
                </html>""";

        enviar(destinatario, asunto, cuerpo);
    }
    public void enviarCambioEstadoTicketCorreo(String destinatario, int id, String servicio, String estado) {
        String asunto = "Cambio de estado del ticket " + id;
        String cuerpo = String.format("""
                <html>
                <head>
                    <style>
                        .header { background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold; }
                        .content { background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black; }
                        .highlight, .service, .state { color: #8D774A; font-weight: bold; }
                    </style>
                </head>
                <body>
                    <div class='header'>Cambio de estado del Ticket</div>
                    <div class='content'>
                        <p>El ticket que creaste de id <span class='highlight'>%d</span> y servicio <span class='service'>%s</span> ha cambiado a estado <span class='state'>%s</span>.</p>
                        <p>Consulta más información dentro de los detalles del ticket del sistema interno.</p>
                    </div>
                </body>
                </html>
                """, id, servicio, estado);
        enviar(destinatario, asunto, cuerpo);
    }

    public void enviarCierreTicketCorreo(String destinatario, int id, String servicio) {
        String asunto = "Cierre del ticket " + id;
        String cuerpo = String.format("""
                <html>
                <head>
                    <style>
                        .header { background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold; }
                        .content { background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black; }
                        .highlight, .service { color: #8D774A; font-weight: bold; }
                    </style>
                </head>
                <body>
                    <div class='header'>Cierre del Ticket</div>
                    <div class='content'>
                        <p>El ticket que creaste de id <span class='highlight'>%d</span> y servicio <span class='service'>%s</span> se ha cerrado.</p>
                        <p>Consulta más información dentro de los detalles del ticket del sistema interno.</p>
                        <p>Esperamos que la solución a tu problema haya resultado ser la más adecuada.</p>
                    </div>
                </body>
                </html>
                """, id, servicio);
        enviar(destinatario, asunto, cuerpo);
    }

    public void enviarAsignacionTicketCorreo(String destinatario, int id, String servicio, String prioridad) {
        String asunto = "Se te ha asignado el ticket " + id;
        String cuerpo = String.format("""
                <html>
                <head>
                    <style>
                        .header { background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold; }
                        .content { background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black; }
                        .highlight, .service, .priority { color: #8D774A; font-weight: bold; }
                    </style>
                </head>
                <body>
                    <div class='header'>Asignación de Ticket</div>
                    <div class='content'>
                        <p>Se te ha asignado la resolución del ticket de id <span class='highlight'>%d</span> y servicio <span class='service'>%s</span> de prioridad <span class='priority'>%s</span>.</p>
                        <p>Consulta más información dentro de los detalles del ticket del sistema interno.</p>
                        <p>Además se espera la pronta resolución de este.</p>
                    </div>
                </body>
                </html>
                """, id, servicio, prioridad);
        enviar(destinatario, asunto, cuerpo);
    }
    public void enviarAsignacionTareaCorreo(String destinatario, String tarea, String prioridad, int id, String servicio) {
        String asunto = "Se te ha asignado la tarea " + tarea + " para el ticket " + id;
        String cuerpo = """
            <html>
            <head>
                <style>
                    .header { background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold; }
                    .content { background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black; }
                    .highlight, .service, .priority, .task { color: #8D774A; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class='header'>Asignación de Tarea</div>
                <div class='content'>
                    <p>Se te ha asignado la resolución de la tarea <span class='task'>%s</span>.</p>
                    <p>La prioridad de la tarea es <span class='priority'>%s</span>.</p>
                    <p>Para el ticket de id <span class='highlight'>%d</span> y servicio <span class='service'>%s</span>.</p>
                    <p>Consulta más información dentro de los detalles del ticket del sistema interno.</p>
                </div>
            </body>
            </html>
            """.formatted(tarea, prioridad, id, servicio);

        enviar(destinatario, asunto, cuerpo);
    }

    public void enviarComentarioTicketCorreo(String destinatario, int id, String nombreRemitente, String servicio, String comentario, String archivo) {
        String asunto = nombreRemitente + " ha comentado en el ticket " + id;
        String archivoHtml = archivo == null || archivo.isEmpty() ? "" : "<p>Se ha enviado un archivo adjunto <a href='" + archivo + "'>aquí</a></p>";

        String cuerpo = """
            <html>
            <head>
                <style>
                    .header { background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold; }
                    .content { background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black; }
                    .highlight, .service, .comment, .sender { color: #8D774A; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class='header'>Comentario sobre el ticket %d</div>
                <div class='content'>
                    <p><span class='sender'>%s</span> ha realizado un comentario sobre el ticket de id <span class='highlight'>%d</span> y servicio <span class='service'>%s</span>.</p>
                    <p class='comment'>“%s”</p>
                    %s
                    <p>Para conocer más detalles revise el sistema de tickets.</p>
                </div>
            </body>
            </html>
            """.formatted(id, nombreRemitente, id, servicio, comentario, archivoHtml);

        enviar(destinatario, asunto, cuerpo);
    }

    public void enviarCambioEstadoTareaCorreo(String destinatario, int id, String remitente, String tarea, String servicio, String estado, boolean finalizado) {
        String asunto = "Cambio de estado de una tarea del ticket " + id;
        String finalizadoTexto = finalizado ? "La tarea ha finalizado" : "La tarea aún no ha finalizado";

        String cuerpo = """
            <html>
            <head>
                <style>
                    .header { background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold; }
                    .content { background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black; }
                    .highlight, .service, .task, .comment, .sender { color: #8D774A; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class='header'>Cambio de estado de una tarea del ticket %d</div>
                <div class='content'>
                    <p><span class='sender'>%s</span> ha realizado un cambio en la tarea <span class='task'>%s</span> del ticket de id <span class='highlight'>%d</span> y servicio <span class='service'>%s</span>.</p>
                    <p>El nuevo estado es:</p>
                    <p class='comment'>“%s”</p>
                    <p>%s</p>
                    <p>Para conocer más detalles revise el sistema de tickets.</p>
                </div>
            </body>
            </html>
            """.formatted(id, remitente, tarea, id, servicio, estado, finalizadoTexto);

        enviar(destinatario, asunto, cuerpo);
    }
    public void enviarBienvenida(Usuario usuario) {
        String destinatario = usuario.getEmail();
        String asunto = "Bienvenido al servicio de Pot1 Tickets";
        String cuerpo = String.format(
                "<html><head><style>" +
                ".header {background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold;}" +
                ".content {background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black;}" +
                ".field {color: #8D774A;}" +
                ".contact {color: #3A5894; text-decoration: underline;}" +
                ".login {color: #3A5894; font-style: italic;}" +
                "</style></head>" +
                "<body><div class='header'>Bienvenido al servicio de Pot1 Tickets</div>" +
                "<div class='content'>" +
                "<p>Se te ha creado un usuario de clase <span class='field'>%s</span></p>" +
                "<p>Tus datos son:</p>" +
                "<p>Nombre: <span class='field'>%s</span></p>" +
                "<p>Apellido: <span class='field'>%s</span></p>" +
                "<p>Teléfono: <span class='field'>%s</span></p>" +
                "<p>Contacto: <span class='field'>%s</span></p>" +
                "<p>Estos son sus datos de login, por favor guárdelos en un lugar seguro</p>" +
                "<p>Correo: <span class='contact'>%s</span></p>" +
                "<p>Contraseña: <span class='login'>%s</span></p>" +
                "<p>Si quiere hacer un cambio de datos más adelante, contacte a este correo por favor.</p>" +
                "</div></body></html>",
                usuario.getRol().getNombre(), usuario.getNombre(), usuario.getApellido(),
                usuario.getTelefono(), usuario.getTelContacto(), usuario.getEmail(), usuario.getContrasena()
        );

        enviar(destinatario, asunto, cuerpo);
    }

    public void enviarCambioDatos(Usuario usuario) {
        String destinatario = usuario.getEmail();
        String asunto = "Cambio de datos al ingreso de Pot1 Tickets";
        String cuerpo = String.format(
                "<html><head><style>" +
                ".header {background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold;}" +
                ".content {background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black;}" +
                ".field {color: #8D774A;}" +
                ".contact {color: #3A5894; text-decoration: underline;}" +
                ".login {color: #3A5894; font-style: italic;}" +
                "</style></head>" +
                "<body><div class='header'>Cambio de datos al ingreso de Pot1 Tickets</div>" +
                "<div class='content'>" +
                "<p>Se le han cambiado los datos de ingreso a la plataforma de Pot1 Tickets</p>" +
                "<p>Tus nuevos datos son:</p>" +
                "<p>Clase: <span class='field'>%s</span></p>" +
                "<p>Nombre: <span class='field'>%s</span></p>" +
                "<p>Apellido: <span class='field'>%s</span></p>" +
                "<p>Teléfono: <span class='field'>%s</span></p>" +
                "<p>Contacto: <span class='field'>%s</span></p>" +
                "<p>Estos son sus nuevos datos de login, por favor guárdelos en un lugar seguro</p>" +
                "<p>Correo: <span class='contact'>%s</span></p>" +
                "<p>Contraseña: <span class='login'>%s</span></p>" +
                "<p>Si quiere hacer un cambio de datos más adelante, contacte a este correo por favor.</p>" +
                "</div></body></html>",
                usuario.getRol().getNombre(), usuario.getNombre(), usuario.getApellido(),
                usuario.getTelefono(), usuario.getTelContacto(), usuario.getEmail(), usuario.getContrasena()
        );

        enviar(destinatario, asunto, cuerpo);
    }

    public void enviarRechazoTareaCorreo(String destinatario, String remitente, String tarea, int id, String servicio) {
        String asunto = String.format("%s ha rechazado una tarea", remitente);
        String cuerpo = String.format(
                "<html><head><style>" +
                ".header {background-color: #2E3B4E; color: white; text-align: center; padding: 10px 0; font-size: 24px; font-weight: bold;}" +
                ".content {background-color: #D3DCE5; padding: 20px; font-family: Arial, sans-serif; font-size: 18px; text-align: center; color: black;}" +
                ".task {color: #8D774A; font-weight: bold;}" +
                ".highlight {color: #8D774A;}" +
                ".sender {color: #8D774A; font-weight: bold;}" +
                "</style></head>" +
                "<body><div class='header'>%s ha rechazado una tarea</div>" +
                "<div class='content'>" +
                "<p><span class='sender'>%s</span> ha rechazado la realización de la tarea <span class='task'>%s</span> del ticket de id <span class='highlight'>%d</span> y del servicio de <span class='highlight'>%s</span></p>" +
                "<p>Reasigna la tarea para que esta se pueda completar, y si crees que la conducta fue inapropiada habla con el administrador o manda un mensaje a este correo.</p>" +
                "</div></body></html>",
                remitente, remitente, tarea, id, servicio
        );

        enviar(destinatario, asunto, cuerpo);
    }
}

