package app.kuriobackend.Services;

import app.kuriobackend.Entities.DTO.UserResponse;
import app.kuriobackend.Entities.Model.User;
import app.kuriobackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    public int registerUser(User user, String password) {
        logger.info("Se va a registrar usuario email='{}'", user.email());
        try {
            int res = repository.register(user, password);
            logger.info("Resultado registerUser: {}", res);
            return res;
        } catch (Exception e) {
            logger.error("Excepción en registerUser con los datos de la excepción: {}", e.toString(), e);
            return -1;
        }
    }

    public User login(String idToken) {
        return repository.login(idToken);
    }

    public int getFollowerCount(String idFollowed) {
        logger.debug("Obteniendo follower count para id='{}'", idFollowed);
        try {
            return repository.findFollowerCount(idFollowed);
        } catch (Exception e) {
            logger.error("Excepción en getFollowerCount: {}", e.toString(), e);
            return 0;
        }
    }

    public int getFollowedCount(String idFollower) {
        return repository.findFollowedCount(idFollower);
    }

    public boolean updateUser(User user, MultipartFile file) {
        logger.info("Se va a actualizar usuario id='{}'", user.id());
        try {
            boolean res = repository.updateUser(user, file);
            logger.info("Resultado updateUser: {}", res);
            return res;
        } catch (Exception e) {
            logger.error("Excepción en updateUser con los datos de la excepción: {}", e.toString(), e);
            return false;
        }
    }

    public UserResponse findById(String id) {
        logger.info("Buscando usuario id='{}'", id);
        try {
            User user = repository.findById(id);
            return user != null ? user.toResponse() : null;
        } catch (Exception e) {
            logger.error("Excepción en findById con los datos de la excepción: {}", e.toString(), e);
            return null;
        }
    }
}

