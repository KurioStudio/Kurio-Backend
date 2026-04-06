package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.UserRequest;
import app.kuriobackend.Entities.DTO.UserResponse;
import app.kuriobackend.Entities.Model.User;
import app.kuriobackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @GetMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody String idToken) {
        User user = service.login(idToken);
        if(user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(user.toResponse());
    }

}
