package app.kuriobackend.Services;

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

    public int updateLike(Post post, String idUser) {
        return repository.like(post, idUser);
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
}
