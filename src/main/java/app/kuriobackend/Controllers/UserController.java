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

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest request){
        User user = User.fromRequest(request);

        if(service.registerUser(user, request.password()) == 0) {
            return ResponseEntity.ok("0");
        } else {
            return ResponseEntity.badRequest().body("-1");
        }
    }

    @GetMapping
    public ResponseEntity<String> login(@AuthenticationPrincipal Jwt jwt) {
        String uid = jwt.getSubject();
        return ResponseEntity.ok(uid);
    }

    @GetMapping("{id}/followers")
    public ResponseEntity<String> followers(@PathVariable String id) {
        int followers = service.getFollowerCount(id);
        return ResponseEntity.ok(followers + "");
    }

    @GetMapping("{id}/followed")
    public ResponseEntity<String> followed(@PathVariable String id) {
        int follower = service.getFollowedCount(id);
        return ResponseEntity.ok(follower + "");
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserRequest request){
        User user = User.fromRequest(request);

        if(service.updateUser(user)){
            return ResponseEntity.ok("0");
        } else {
            return ResponseEntity.badRequest().body("-1");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        UserResponse response = service.findById(id);
        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
