package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.User;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
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

            //Despues lo registramos en la base de datos
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(user.id()).set(user).get();
            return res;
        } catch (Exception e) {
            res = 1;
            return res;
        }
    }

}
