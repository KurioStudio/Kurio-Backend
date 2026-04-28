package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.UserRequest;
import app.kuriobackend.Entities.DTO.UserResponse;
import app.kuriobackend.Entities.Model.User;
import app.kuriobackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/followers")
    public ResponseEntity<String> followers(@RequestBody String idFollowed) {
        int followers = service.getFollowerCount(idFollowed);
        return ResponseEntity.ok(followers + "");
    }

    @GetMapping("/followed")
    public ResponseEntity<String> followed(@RequestBody String idFollower) {
        int follower = service.getFollowedCount(idFollower);
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

}
