package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.Follow;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;


@Repository
public class FollowsRepository {

    private final String COLLECTION = "follows";

    public int follow(Follow follow) {
        int res = 0;
        try {
            System.out.println("Guardando follow: " + follow.idFollower() + " -> " + follow.idFollowed());
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION)
            .document(follow.idFollower() + "_" + follow.idFollowed())
            .set(follow)
            .get();

            return res;
        } catch (Exception e) {
            e.printStackTrace(); 
            return -1;
        }
    }

    public int unfollow(Follow follow) {
        int res = 0;
        try {
            Firestore db = FirestoreClient.getFirestore();
            System.out.println("Eliminando follow: " + follow.idFollower() + " sigue a " + follow.idFollowed());
            db.collection(COLLECTION).document(follow.idFollower() + "_" + follow.idFollowed()).delete().get();
            return res;
        } catch (Exception e) {
            System.out.println("Error al eliminar el follow: " + e.getMessage());
            res = -1;
            return res;
        }
    }

}
