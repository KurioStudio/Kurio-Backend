package app.kuriobackend.Services;

import app.kuriobackend.Entities.Model.Follow;
import app.kuriobackend.Repositories.FollowsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    FollowsRepository repository;

    public int follow(Follow follow) {
        return repository.follow(follow);
    }

    public int unfollow(Follow follow) {
        return repository.unfollow(follow);
    }

    public boolean isFollowing(Follow follow) {
        return repository.isFollowing(follow);
    }

}
