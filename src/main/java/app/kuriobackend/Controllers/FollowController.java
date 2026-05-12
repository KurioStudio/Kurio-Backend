package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.FollowRequest;
import app.kuriobackend.Entities.Model.Follow;
import app.kuriobackend.Services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080", "http://localhost:80", "http://kurio.duckdns.org"})
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService service;
    private static final Logger logger = LogManager.getLogger(FollowController.class);

    @GetMapping()
    public ResponseEntity<String> follow(@Validated FollowRequest followRequest) {
        logger.info("Se va a crear follow: follower='{}' -> followed='{}'", followRequest.idFollower(), followRequest.idFollowed());
        try {
            Follow follow = Follow.fromRequest(followRequest);
            if(service.follow(follow) == 0) {
                logger.info("Follow creado: follower='{}' -> followed='{}'", followRequest.idFollower(), followRequest.idFollowed());
                return ResponseEntity.ok("0");
            } else {
                logger.error("Excepción en follow para follower='{}'", followRequest.idFollower());
                return  ResponseEntity.badRequest().body("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en follow con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> unfollow(@Validated FollowRequest followRequest) {
        logger.info("Se va a eliminar follow: follower='{}' -> followed='{}'", followRequest.idFollower(), followRequest.idFollowed());
        try {
            Follow follow = Follow.fromRequest(followRequest);
            if(service.unfollow(follow) == 0) {
                logger.info("Follow eliminado: follower='{}' -> followed='{}'", followRequest.idFollower(), followRequest.idFollowed());
                return ResponseEntity.ok("0");
            } else {
                logger.error("Excepción en unfollow para follower='{}'", followRequest.idFollower());
                return  ResponseEntity.badRequest().body("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en unfollow con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }

    @PostMapping("/isFollowing")
    public ResponseEntity<String> isFollowing(@RequestBody @Validated FollowRequest followRequest) {
        logger.info("Se va a comprobar isFollowing: follower='{}' -> followed='{}'", followRequest.idFollower(), followRequest.idFollowed());
        try {
            Follow follow = Follow.fromRequest(followRequest);
            if(service.isFollowing(follow)) {
                logger.info("isFollowing=true for follower='{}'->followed='{}'", followRequest.idFollower(), followRequest.idFollowed());
                return ResponseEntity.ok("0");
            } else {
                logger.info("isFollowing=false for follower='{}'->followed='{}'", followRequest.idFollower(), followRequest.idFollowed());
                return  ResponseEntity.ok("-1");
            }
        } catch (Exception e) {
            logger.error("Excepción en isFollowing con los datos de la excepción: {}", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1");
        }
    }
}
