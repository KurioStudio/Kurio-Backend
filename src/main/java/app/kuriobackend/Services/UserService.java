package app.kuriobackend.Services;

import app.kuriobackend.Entities.DTO.UserResponse;
import app.kuriobackend.Entities.Model.User;
import app.kuriobackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public int registerUser(User user, String password) {
        return repository.register(user, password);
    }

    public User login(String idToken) {
        return repository.login(idToken);
    }

    public int getFollowerCount(String idFollowed) {
        return repository.findFollowerCount(idFollowed);
    }

    public int getFollowedCount(String idFollower) {
        return repository.findFollowedCount(idFollower);
    }

}
