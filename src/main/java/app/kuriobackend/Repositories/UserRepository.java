package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.User;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

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

}
