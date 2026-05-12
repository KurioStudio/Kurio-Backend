package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.ComentarioRequest;
import app.kuriobackend.Entities.DTO.ComentarioResponse;
import app.kuriobackend.Entities.Model.Comentario;
import app.kuriobackend.Services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080", "http://localhost:80", "http://kurio.duckdns.org"})
@RequestMapping("/api/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioService service;
    private static final Logger logger = LogManager.getLogger(ComentarioController.class);

    @PostMapping("/post")
    public ResponseEntity<ArrayList<ComentarioResponse>> mostrarComentariosPost(@RequestBody Map<String, String> payload) {
        String idPost = payload.get("idPost");
        List<Comentario> comentarios = service.mostrarComentariosPost(idPost);
        logger.info("Se van a obtener comentarios para idPost='{}'", idPost);
        logger.debug("Comentarios: {}", comentarios);

        if(comentarios!=null && !comentarios.isEmpty()){
            ArrayList<ComentarioResponse> comentarioResponse = new ArrayList<>();
            for (Comentario comentario : comentarios) {
                comentarioResponse.add(comentario.toResponse());
            }
            return ResponseEntity.ok(comentarioResponse);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
    }

    @PostMapping
    public ResponseEntity<String> guardar(@RequestBody ComentarioRequest comentarioRequest) {
        logger.info("Se va a guardar comentario para postId='{}' by user='{}'", comentarioRequest.idPost(), comentarioRequest.idUser());
        try {
            int res = service.guardar(Comentario.fromRequest(comentarioRequest));
            if(res == 0) {
                logger.info("Comentario guardado correctamente para postId='{}'", comentarioRequest.idPost());
                return ResponseEntity.ok("0");
            } else {
                logger.error("Excepción en guardarComentario resultado={}", res);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en guardarComentario con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }
    @DeleteMapping("/{idComentario}")
    public ResponseEntity<String> eliminarComentario(@PathVariable String idComentario) {
        logger.info("Se va a eliminar comentario con id='{}'", idComentario);
        try {
            int res = service.eliminarComentario(idComentario);
            if(res == 0) {
                logger.info("Comentario eliminado correctamente con id='{}'", idComentario);
                return ResponseEntity.ok("0");
            } else {
                logger.error("Excepción en eliminarComentario resultado={}", res);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en eliminarComentario con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }
}
