package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.dto.NotificacionDTO;
import co.analisys.biblioteca.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificar")
@Tag(name = "Notificaciones", description = "APIs para el envío de notificaciones a usuarios")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @Operation(summary = "Enviar notificación", description = "Envía una notificación a un usuario del sistema. Se utiliza para comunicar eventos relacionados con préstamos, devoluciones o recordatorios. Solo los bibliotecarios pueden enviar notificaciones.", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación enviada exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de notificación inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "No autorizado - Requiere rol LIBRARIAN", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public void enviarNotificacion(
            @Parameter(description = "Datos de la notificación a enviar", required = true, content = @Content(schema = @Schema(implementation = NotificacionDTO.class), examples = @ExampleObject(name = "Recordatorio de devolución", value = "{\n  \"destinatario\": \"usuario@ejemplo.com\",\n  \"asunto\": \"Recordatorio de devolución\",\n  \"mensaje\": \"Le recordamos que debe devolver el libro 'Clean Code' antes del 15 de marzo de 2026.\"\n}"))) @RequestBody NotificacionDTO notificacion) {
        notificacionService.enviarNotificacion(notificacion);
    }

    @Operation(summary = "Estado del servicio", description = "Endpoint público para verificar el estado del servicio de notificaciones. No requiere autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio disponible", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/public/status")
    public String getPublicStatus() {
        return "El servicio de notificaciones está funcionando correctamente";
    }
}