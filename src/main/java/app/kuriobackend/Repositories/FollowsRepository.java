package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.DTO.FollowResponse;
import app.kuriobackend.Entities.Model.Follow;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class FollowsRepository {

    private final String COLLECTION = "follows";

    public int follow(Follow follow) {
        int res = 0;
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(follow.idFollower() + "_" + follow.idFollowed()).set(follow);
            return res;
        } catch (Exception e) {
            res = -1;
            return res;
        }
    }

    public int unfollow(Follow follow) {
        int res = 0;
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(follow.idFollower() + "_" + follow.idFollowed()).delete();
            return res;
        } catch (Exception e) {
            res = -1;
            return res;
        }
    }

}
