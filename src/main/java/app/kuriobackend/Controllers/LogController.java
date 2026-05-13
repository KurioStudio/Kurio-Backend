package app.kuriobackend.Controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("")
public class LogController {

    private static final Logger logger = LogManager.getLogger(LogController.class);
    private static final String LOGS_DIR = "logs";

    /**
     * Servir cualquier fichero .log desde la carpeta logs/
     * Ejemplo: GET /posts.log, /users.log, /follow.log, /comentarios.log, /app.log, etc.
     */
    @GetMapping("/{filename:.+\\.log}")
    public ResponseEntity<String> serveLogFile(@PathVariable String filename) {
        try {
            logger.info("Se va a acceder al fichero de log: {}", filename);

            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                logger.warn("Se intentó path traversal en logs con filename: {}", filename);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
            }

            String logPath = Paths.get(LOGS_DIR, filename).toString();
            File logFile = new File(logPath);

            if (!logFile.exists()) {
                logger.warn("Fichero de log no encontrado: {}", filename);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Fichero de log no encontrado: " + filename);
            }

            String content = Files.readString(Paths.get(logPath));
            logger.info("Se ha leído fichero de log correctamente: {}, tamaño: {} bytes", filename, content.length());

            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body(content);

        } catch (IOException e) {
            logger.error("Excepción al leer fichero de log '{}' con los datos de la excepción: {}", filename, e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al leer el fichero de log: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Excepción inesperada al leer log '{}': {}", filename, e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }
}

