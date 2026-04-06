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
                    user.createrAt()
            );

            //Despues lo registramos en la base de datos
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(user.id()).set(firebaseUser).get();
            return res;
        } catch (Exception e) {
            res = 1;
            return res;
        }
    }

    public User login(String idToken) {
        User user = null;
        try {
            //Recogemos el token con el UID del usuario
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = token.getUid();

            if(uid != null && !uid.isEmpty()){
                //Buscamos en la base de datos al usuario con el UID.
                Firestore db = FirestoreClient.getFirestore();
                user = db.collection(COLLECTION).document(uid).get().get().toObject(User.class);
                return user;
            }
            return null;
        } catch (FirebaseAuthException | ExecutionException | InterruptedException e) {
            return null;
        }
    }

}
