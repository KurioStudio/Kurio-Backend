package app.kuriobackend.Services;

import app.kuriobackend.Entities.Model.Comentario;
import app.kuriobackend.Repositories.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository repository;
    private static final Logger logger = LogManager.getLogger(ComentarioService.class);

    public int guardar(Comentario comentario) {
        logger.info("Se va a guardar comentario postId='{}' userId='{}'", comentario.idPost(), comentario.idUser());
        try {
            logger.debug("Comentario a guardar {}", comentario);
            int res = repository.guardar(comentario);
            logger.info("Resultado guardarComentario: {}", res);
            return res;
        } catch (Exception e) {
            logger.error("Excepción en guardarComentario con los datos de la excepción: {}", e.toString(), e);
            return -1;
        }
    }

    public List<Comentario> mostrarComentariosPost(String idPost) {
        logger.info("Se van a obtener comentarios para postId='{}'", idPost);
        try {
            return repository.mostrarComentariosPost(idPost);
        } catch (Exception e) {
            logger.error("Excepción en mostrarComentariosPost con los datos de la excepción: {}", e.toString(), e);
            return new java.util.ArrayList<>();
        }
    }

}
