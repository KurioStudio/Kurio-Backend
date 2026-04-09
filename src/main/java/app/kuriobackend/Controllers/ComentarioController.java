package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.ComentarioRequest;
import app.kuriobackend.Entities.DTO.ComentarioResponse;
import app.kuriobackend.Entities.Model.Comentario;
import app.kuriobackend.Services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioService service;

    @GetMapping("/post")
    public ArrayList<ComentarioResponse> mostrarComentariosPost(@RequestBody String idPost) {
        List<Comentario> comentarios = service.mostrarComentariosPost(idPost);

        if(comentarios!=null && !comentarios.isEmpty()){
            ArrayList<ComentarioResponse> comentarioResponse = new ArrayList<>();
            for (Comentario comentario : comentarios) {
                comentarioResponse.add(comentario.toResponse());
            }
            return comentarioResponse;
        }

        return new ArrayList<>();
    }

    @PostMapping
    public int guardar(@RequestBody ComentarioRequest comentarioRequest) {
        return service.guardar(Comentario.fromRequest(comentarioRequest));
    }

}
