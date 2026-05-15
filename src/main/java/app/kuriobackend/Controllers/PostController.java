package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.FollowRequest;
import app.kuriobackend.Entities.DTO.GuardadoRequest;
import app.kuriobackend.Entities.DTO.PostRequest;
import app.kuriobackend.Entities.DTO.PostResponse;
import app.kuriobackend.Entities.Model.Post;
import app.kuriobackend.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080", "http://localhost:80", "http://kurio.duckdns.org"})
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService service;
    private static final Logger logger = LogManager.getLogger(PostController.class);

    @PostMapping
    public ResponseEntity<String> create(
            @RequestPart("request") PostRequest request,
            @RequestPart("imagenes") List<MultipartFile> imagenes ,
            @RequestPart("file") MultipartFile file
    ) {
        logger.info("Se va a crear post con title='{}', userId='{}', imagenesCount={}, fileName={}",
                request.titulo(), request.user().id(), imagenes != null ? imagenes.size() : 0,
                file != null ? file.getOriginalFilename() : "null");
        try {
            Post post = Post.fromRequest(request);
            int resultado = service.guardarPost(post, imagenes, file);

            if(resultado == 0){
                logger.info("Se ha creado el post correctamente para userId='{}', title='{}'", request.user().id(), request.titulo());
                return ResponseEntity.status(HttpStatus.CREATED).body("0");
            } else {
                logger.error("Excepción en crearPost con los datos de la operación: resultado={}", resultado);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en crearPost con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody String id, @RequestBody PostRequest request) {
        logger.info("Se va a actualizar post id='{}' con datos del request", id);
        try {
            Post post = Post.fromRequest(request);
            int resultado = service.actualizarPost(id, post);
            if(resultado == 0){
                logger.info("Se ha actualizado el post id='{}' correctamente", id);
                return ResponseEntity.status(HttpStatus.OK).body("0");
            } else {
                logger.error("Excepción en actualizarPost con los datos de la operación: resultado={}", resultado);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en actualizarPost con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
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
        System.out.println("Posts encontrados para el seguidor " + idFollower.idFollower() + ": " + posts.size());
        if(posts != null && !posts.isEmpty()){
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.ok(postResponses);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
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

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarPostLista(@RequestBody GuardadoRequest request) {
        int resultado = service.guardarPostLista(request);

        if(resultado == 0){
            return ResponseEntity.status(HttpStatus.CREATED).body("0");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
        }
    }

    @DeleteMapping("/guardar")
    public ResponseEntity<String> eliminarPostLista(@RequestBody GuardadoRequest request) {
        int resultado = service.eliminarPostLista(request);

        if(resultado == 0){
            return ResponseEntity.status(HttpStatus.OK).body("0");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
        }
    }

    @GetMapping("/{idUser}/guardados")
    public ResponseEntity<List<PostResponse>> findGuardadosByUser(@PathVariable String idUser) {
        List<Post> posts = service.findGuardadosByUser(idUser);
        if(posts != null && !posts.isEmpty()) {
            List<PostResponse> postResponses = new ArrayList<>();
            posts.forEach(post -> {
                postResponses.add(post.toResponse());
            });
            return ResponseEntity.ok(postResponses);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
    }

    @PostMapping("/isGuardado")
    public ResponseEntity<Boolean> isPostGuardado(@RequestBody GuardadoRequest request) {
        boolean resultado = service.isPostGuardado(request);
        return ResponseEntity.ok(resultado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPost(@PathVariable String id) {
        logger.info("Se va a eliminar el post id='{}'", id);
        try{
            int resultado = service.eliminarPost(id);
            if(resultado == 0){
                logger.info("Se ha eliminado el post id='{}' correctamente", id);
                return ResponseEntity.status(HttpStatus.OK).body("0");
            } else {
                logger.error("Excepción en eliminarPost con los datos de la operación: resultado={}", resultado);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1");
            }
        }catch(Exception e){
            logger.error("Error al eliminar el post id='{}'", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
