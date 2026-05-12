package app.kuriobackend.Services;

import app.kuriobackend.Entities.DTO.GuardadoRequest;
import app.kuriobackend.Entities.DTO.PostResponse;
import app.kuriobackend.Entities.Model.Post;
import app.kuriobackend.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;
    private static final Logger logger = LogManager.getLogger(PostService.class);

    public int guardarPost(Post post, List<MultipartFile> imagenes, MultipartFile file) {
        logger.info("Se va a guardar post title='{}' userId='{}' imagenesCount={}", post.titulo(), post.user(), imagenes != null ? imagenes.size() : 0);
        try {
            int res = repository.guardarPost(post, imagenes, file);
            logger.info("Resultado guardarPost: {}", res);
            return res;
        } catch (Exception e) {
            logger.error("Excepción en guardarPost con los datos de la excepción: {}", e.toString(), e);
            return -1;
        }
    }

    public int actualizarPost(String id, Post post) {
        return repository.actualizarPost(id, post);
    }

    public List<Post> findAll() {
        try {
            logger.info("Se van a obtener todos los posts");
            return repository.findAll();
        } catch (Exception e) {
            logger.error("Excepción en findAll con los datos de la excepción: {}", e.toString(), e);
            return new ArrayList<>();
        }
    }

    public int updateLike(String idPost, String idUser) {
        logger.info("Se va a actualizar like para postId='{}' por userId='{}'", idPost, idUser);
        try {
            int res = repository.like(idPost, idUser);
            logger.info("Resultado updateLike: {}", res);
            return res;
        } catch (Exception e) {
            logger.error("Excepción en updateLike con los datos de la excepción: {}", e.toString(), e);
            return -1;
        }
    }

    public List<Post> findAllByTitle(String title) {
        return repository.findAllByTitle(title);
    }

    public List<Post> findFollowed(String idFollower) {
        return repository.findFollowed(idFollower);
    }

    public GridFsResource descargarArchivo(String oid) {
        logger.info("Se va a descargar archivo oid='{}'", oid);
        try {
            GridFsResource res = repository.descargarArchivo(oid);
            logger.info("Archivo descargado oid='{}' filename={}", oid, res != null ? res.getFilename() : "null");
            return res;
        } catch (Exception e) {
            logger.error("Excepción en descargarArchivo con los datos de la excepción: {}", e.toString(), e);
            return null;
        }
    }

    public List<Post> findTopPosts() {
        return repository.findTopPosts();
    }

    public List<Post> findRecentPosts() {
        return repository.findRecentPosts();
    }

    public PostResponse findById(String id) {
        Post post = repository.findById(id);
        return post != null ? post.toResponse() : null;
    }

    public int guardarPostLista(GuardadoRequest request) {
        return repository.guardarPostLista(request);
    }

    public int eliminarPostLista(GuardadoRequest request) {
        return repository.eliminarPostLista(request);
    }

    public List<Post> findGuardadosByUser(String idUser) {
        return repository.findGuardadosByUser(idUser);
    }

    public boolean isPostGuardado(GuardadoRequest request) {
        return repository.isPostGuardado(request);
    }
}
