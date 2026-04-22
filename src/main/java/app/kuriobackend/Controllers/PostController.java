package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.PostRequest;
import app.kuriobackend.Entities.DTO.PostResponse;
import app.kuriobackend.Entities.Model.Post;
import app.kuriobackend.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService service;

    @PostMapping
    public ResponseEntity<String> create(
            @RequestPart("request") PostRequest request,
            @RequestPart("imagenes") List<MultipartFile> imagenes ,
            @RequestPart("file") MultipartFile file
    ) {
        Post post = Post.fromRequest(request);
        int resultado = service.guardarPost(post, imagenes, file);

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
    public ResponseEntity<String> like(@RequestBody PostRequest request) {
        String idUser = null;
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            idUser = jwt.getSubject();
        }

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

    @GetMapping("/top")
    public ResponseEntity<List<PostResponse>> findTopPosts() {
        List<Post> posts = service.findTopPosts();
        if(posts != null && !posts.isEmpty()) {
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.ok(postResponses);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PostResponse>> findRecentPosts() {
        List<Post> posts = service.findRecentPosts();
        if(posts != null && !posts.isEmpty()) {
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.ok(postResponses);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
    }

    @GetMapping("/descargar/{oid}")
    public ResponseEntity<InputStreamResource> descargarArchivo(@PathVariable String oid) throws IOException {
        GridFsResource file = service.descargarArchivo(oid);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getContentType())).body(new InputStreamResource(file.getInputStream()));
    }
}
