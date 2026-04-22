package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {

    private final String COLLECTION = "users";
    private final String FOLLOW_COLLECTION = "follow";

    public int register(User user, String password){
        int res = 0;
        try {
            //Primero añadimos el usuario a Firebase Auth con sus datos
            FirebaseAuth auth = FirebaseAuth.getInstance();
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(user.email())
                    .setPassword(password)
                    .setDisplayName(user.username());

            UserRecord record = auth.createUser(request);
            User firebaseUser = new User(
                    record.getUid(),
                    user.username(),
                    user.email(),
                    "",
                    user.createdAt()
            );

            //Despues lo registramos en la base de datos
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(firebaseUser.email()).set(firebaseUser).get();
            return res;
        } catch (Exception e) {
            res = 1;
            e.printStackTrace();
            return res;
        }
    }

    public User login(String idToken) {
        User user = null;
        try {
            //Recogemos el token con el UID del usuario
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = token.getEmail();

            if(email != null && !email.isEmpty()){
                //Buscamos en la base de datos al usuario con el UID.
                Firestore db = FirestoreClient.getFirestore();
                user = db.collection(COLLECTION).document(email).get().get().toObject(User.class);
                return user;
            }
            return null;
        } catch (FirebaseAuthException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int findFollowerCount(String idFollowed) {
        int followerCount = 0;
        try {
            Firestore db = FirestoreClient.getFirestore();
            followerCount = db.collection(FOLLOW_COLLECTION).whereEqualTo("idFollowed", idFollowed).get().get().size();
            return followerCount;
        } catch (Exception e) {
            followerCount = -1;
            return followerCount;
        }
    }

    public int findFollowedCount(String idFollower) {
        int followedCount = 0;
        try {
            Firestore db = FirestoreClient.getFirestore();
            followedCount = db.collection(FOLLOW_COLLECTION).whereEqualTo("idFollowed", idFollower).get().get().size();
            return followedCount;
        } catch (Exception e) {
            followedCount = -1;
            return followedCount;
        }
    }

    public boolean updateUser(User user) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(user.email()).set(user).get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
