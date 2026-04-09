package app.kuriobackend.Services;

import app.kuriobackend.Entities.Model.Comentario;
import app.kuriobackend.Repositories.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository repository;

    public int guardar(Comentario comentario) {
        return repository.guardar(comentario);
    }

    public List<Comentario> mostrarComentariosPost(String idPost) {
        return repository.mostrarComentariosPost(idPost);
    }

}
