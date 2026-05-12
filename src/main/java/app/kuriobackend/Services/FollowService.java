package app.kuriobackend.Services;

import app.kuriobackend.Entities.Model.Follow;
import app.kuriobackend.Repositories.FollowsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class FollowService {

    @Autowired
    FollowsRepository repository;
    private static final Logger logger = LogManager.getLogger(FollowService.class);

    public int follow(Follow follow) {
        logger.info("Se va a crear follow follower='{}' -> followed='{}'", follow.idFollower(), follow.idFollowed());
        try {
            int res = repository.follow(follow);
            logger.info("Resultado follow: {}", res);
            return res;
        } catch (Exception e) {
            logger.error("Excepción en follow con los datos de la excepción: {}", e.toString(), e);
            return -1;
        }
    }

    public int unfollow(Follow follow) {
        logger.info("Se va a eliminar follow follower='{}' -> followed='{}'", follow.idFollower(), follow.idFollowed());
        try {
            int res = repository.unfollow(follow);
            logger.info("Resultado unfollow: {}", res);
            return res;
        } catch (Exception e) {
            logger.error("Excepción en unfollow con los datos de la excepción: {}", e.toString(), e);
            return -1;
        }
    }

    public boolean isFollowing(Follow follow) {
        logger.debug("Comprobando isFollowing follower='{}' -> followed='{}'", follow.idFollower(), follow.idFollowed());
        try {
            return repository.isFollowing(follow);
        } catch (Exception e) {
            logger.error("Excepción en isFollowing con los datos de la excepción: {}", e.toString(), e);
            return false;
        }
    }

}
