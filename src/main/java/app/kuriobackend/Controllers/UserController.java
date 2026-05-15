package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.UserRequest;
import app.kuriobackend.Entities.DTO.UserResponse;
import app.kuriobackend.Entities.Model.User;
import app.kuriobackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080", "http://localhost:80", "https://kurio.duckdns.org"})
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest request){
        logger.info("Se va a registrar usuario con email='{}'", request.email());
        try {
            User user = User.fromRequest(request);
            if(service.registerUser(user, request.password()) == 0) {
                logger.info("Usuario registrado correctamente email='{}'", request.email());
                return ResponseEntity.ok("0");
            } else {
                logger.error("Excepción en registerUser para email='{}'", request.email());
                return ResponseEntity.badRequest().body("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en registerUser con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }

    @GetMapping
    public ResponseEntity<String> login(@AuthenticationPrincipal Jwt jwt) {
        String uid = jwt.getSubject();
        logger.info("Login solicitado para uid='{}'", uid);
        return ResponseEntity.ok(uid);
    }

    @GetMapping("{id}/followers")
    public ResponseEntity<String> followers(@PathVariable String id) {
        logger.info("Se van a obtener followers para userId='{}'", id);
        try {
            int followers = service.getFollowerCount(id);
            logger.info("Followers obtenidos para userId='{}' count={}", id, followers);
            return ResponseEntity.ok(followers + "");
        } catch (Exception e) {
            logger.error("Excepción en followers con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }

    @GetMapping("{id}/followed")
    public ResponseEntity<String> followed(@PathVariable String id) {
        logger.info("Se van a obtener followed para userId='{}'", id);
        try {
            int follower = service.getFollowedCount(id);
            logger.info("Followed obtenidos para userId='{}' count={}", id, follower);
            return ResponseEntity.ok(follower + "");
        } catch (Exception e) {
            logger.error("Excepción en followed con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestPart("request") UserRequest request, @RequestPart("file") MultipartFile file) {
        logger.info("Se va a actualizar usuario email='{}'", request.email());
        try {
            User user = User.fromRequest(request);
            if(service.updateUser(user, file)){
                logger.info("Usuario actualizado correctamente email='{}'", request.email());
                return ResponseEntity.ok("0");
            } else {
                logger.error("Excepción en updateUser email='{}'", request.email());
                return ResponseEntity.badRequest().body("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en updateUser con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        logger.info("Se va a buscar usuario por id='{}'", id);
        try {
            UserResponse response = service.findById(id);
            if(response != null){
                logger.info("Usuario encontrado id='{}'", id);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                logger.info("Usuario no encontrado id='{}'", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            logger.error("Excepción en findById con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
