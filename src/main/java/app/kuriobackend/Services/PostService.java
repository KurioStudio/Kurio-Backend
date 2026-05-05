package app.kuriobackend.Services;

import app.kuriobackend.Entities.DTO.GuardadoRequest;
import app.kuriobackend.Entities.DTO.PostResponse;
import app.kuriobackend.Entities.Model.Post;
import app.kuriobackend.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    public int guardarPost(Post post, List<MultipartFile> imagenes, MultipartFile file) {
        return repository.guardarPost(post, imagenes, file);
    }

    public int actualizarPost(String id, Post post) {
        return repository.actualizarPost(id, post);
    }

    public List<Post> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public int updateLike(String idPost, String idUser) {
        return repository.like(idPost, idUser);
    }

    public List<Post> findAllByTitle(String title) {
        return repository.findAllByTitle(title);
    }

    public List<Post> findFollowed(String idFollower) {
        return repository.findFollowed(idFollower);
    }

    public GridFsResource descargarArchivo(String oid) {
        return repository.descargarArchivo(oid);
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
}
