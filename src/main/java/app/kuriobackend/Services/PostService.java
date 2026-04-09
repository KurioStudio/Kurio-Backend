package app.kuriobackend.Services;

import app.kuriobackend.Entities.Model.Post;
import app.kuriobackend.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    public int guardarPost(Post post) {
        return repository.guardarPost(post);
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

}
