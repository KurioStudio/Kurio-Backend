package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.FollowRequest;
import app.kuriobackend.Entities.Model.Follow;
import app.kuriobackend.Services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080", "http://localhost:80", "http://kurio.duckdns.org"})
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService service;

    @GetMapping()
    public ResponseEntity<String> follow(@Validated FollowRequest followRequest) {
        Follow follow = Follow.fromRequest(followRequest);
        if(service.follow(follow) == 0) {
            return ResponseEntity.ok("0");
        } else {
            return  ResponseEntity.badRequest().body("-1");
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> unfollow(@Validated FollowRequest followRequest) {
        Follow follow = Follow.fromRequest(followRequest);
        if(service.unfollow(follow) == 0) {
            return ResponseEntity.ok("0");
        } else {
            return  ResponseEntity.badRequest().body("-1");
        }
    }

    @PostMapping("/isFollowing")
    public ResponseEntity<String> isFollowing(@RequestBody @Validated FollowRequest followRequest) {
        Follow follow = Follow.fromRequest(followRequest);
        if(service.isFollowing(follow)) {
            return ResponseEntity.ok("0");
        } else {
            return  ResponseEntity.ok("-1");
        }
    }
}
