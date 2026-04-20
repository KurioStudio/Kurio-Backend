package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.PostRequest;
import app.kuriobackend.Entities.DTO.PostResponse;
import app.kuriobackend.Entities.Model.Post;
import app.kuriobackend.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService service;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody PostRequest request) {
        Post post = Post.fromRequest(request);
        int resultado = service.guardarPost(post);

        if(resultado == 0){
            return ResponseEntity.status(HttpStatus.CREATED).body("0");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
        }
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody String id, @RequestBody PostRequest request) {
        Post post = Post.fromRequest(request);
        int resultado = service.actualizarPost(id, post);
        if(resultado == 0){
            return ResponseEntity.status(HttpStatus.OK).body("0");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
        }
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        List<Post> posts = service.findAll();

        if(posts != null && !posts.isEmpty()){
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.status(HttpStatus.OK).body(postResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/like")
    public ResponseEntity<String> like(@RequestBody PostRequest request, @RequestParam String idUser) {
        Post post = Post.fromRequest(request);
        int resultado = service.updateLike(post, idUser);
        if(resultado == 0){
            return ResponseEntity.status(HttpStatus.CREATED).body("0");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
        }
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostResponse>> findByTitle(@RequestBody String title) {
        List<Post> posts = service.findAllByTitle(title);
        if(posts != null && !posts.isEmpty()) {
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.ok(postResponses);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
    }

    @GetMapping("/follow")
    public ResponseEntity<List<PostResponse>> findByFollower(@RequestBody String idFollower) {
        List<Post> posts = service.findFollowed(idFollower);
        if(posts != null && !posts.isEmpty()){
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.ok(postResponses);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
    }

    //TODO: Descarga del fichero del modelo
}
