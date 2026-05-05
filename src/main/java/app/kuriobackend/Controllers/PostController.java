package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.FollowRequest;
import app.kuriobackend.Entities.DTO.PostRequest;
import app.kuriobackend.Entities.DTO.PostResponse;
import app.kuriobackend.Entities.Model.Post;
import app.kuriobackend.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
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
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
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

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable String id) {
        PostResponse response = service.findById(id);
        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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

    @PutMapping("/{idPost}/like")
    public ResponseEntity<String> like(@PathVariable String idPost) {
        String idUser = null;
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            idUser = jwt.getSubject();
        }

        int resultado = service.updateLike(idPost, idUser);
        if(resultado == 0){
            return ResponseEntity.status(HttpStatus.CREATED).body("0");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
        }
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostResponse>> findByTitle(@RequestParam String title) {
        List<Post> posts = service.findAllByTitle(title);
        if(posts != null && !posts.isEmpty()) {
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.ok(postResponses);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
    }

    @PostMapping("/follow")
    public ResponseEntity<List<PostResponse>> findByFollower(@RequestBody FollowRequest idFollower) {
        List<Post> posts = service.findFollowed(idFollower.idFollower());
        if(posts != null && !posts.isEmpty()){
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.ok(postResponses);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
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

    @GetMapping("/{oid}/descargar")
    public ResponseEntity<InputStreamResource> descargarArchivo(@PathVariable String oid) throws IOException {
    GridFsResource file = service.descargarArchivo(oid);

    String filename = file.getFilename();
    String contentType = file.getContentType();

    if (filename == null || !filename.contains(".")) {
        String extension = "stl";

        if (contentType != null) {
            if (contentType.toLowerCase().contains("3mf")) {
                extension = "3mf";
            } else if (contentType.toLowerCase().contains("stl")) {
                extension = "stl";
            }
        }

        filename = (filename != null ? filename : "modelo") + "." + extension;
    }

    MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

    if (filename.toLowerCase().endsWith(".3mf")) {
        mediaType = MediaType.parseMediaType("application/vnd.ms-package.3dmanufacturing-3dmodel+xml");
    } else if (filename.toLowerCase().endsWith(".stl")) {
        mediaType = MediaType.parseMediaType("model/stl");
    }

    return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filename + "\"")
            .body(new InputStreamResource(file.getInputStream()));
    }
}
