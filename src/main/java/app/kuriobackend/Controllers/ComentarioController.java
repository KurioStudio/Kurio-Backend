package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.ComentarioRequest;
import app.kuriobackend.Entities.DTO.ComentarioResponse;
import app.kuriobackend.Entities.Model.Comentario;
import app.kuriobackend.Services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080", "http://localhost:80", "http://kurio.duckdns.org"})
@RequestMapping("/api/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioService service;

    @PostMapping("/post")
    public ResponseEntity<ArrayList<ComentarioResponse>> mostrarComentariosPost(@RequestBody Map<String, String> payload) {
        String idPost = payload.get("idPost");
        List<Comentario> comentarios = service.mostrarComentariosPost(idPost);
        System.out.println("Comentarios: " + comentarios);

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
        int res = service.guardar(Comentario.fromRequest(comentarioRequest));
        if(res == 0) {
            return ResponseEntity.ok("0");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
        }
    }

}
