package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.Post;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class PostRepository {

    private final String COLLECTION = "posts";

    public int guardarPost(Post post) {
        int res = 0;
        try {
            //Recogemos el ultimo ID de la base de datos
            String id = String.valueOf(Integer.parseInt(getUltimoId()) + 1);

            //Creamos otro objeto Post para añadirle el id
            Post firebasePost = new Post(
                    id,
                    post.titulo(),
                    post.descripcion(),
                    post.imagenes(),
                    null,
                    null,
                    post.user(),
                    post.oid(),
                    post.licencia(),
                    post.createdAt()
            );

            //Insertamos el nuevo post
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(id).set(firebasePost).get();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            res = 1;
            return res;
        }
    }
    
    public int actualizarPost(String id, Post post) {
        int res = 0;
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(id).set(post);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            res = -1;
            return res;
        }
    }

    private String getUltimoId() {
        try {
            //Recogemos de la base de datos el ultimo ID del ultimo post ordenado por la fecha de creacion
            Firestore db = FirestoreClient.getFirestore();
            Optional<Post> post = db.collection(COLLECTION).limitToLast(1).orderBy("createdAt").get().get().toObjects(Post.class).stream().findFirst();
            if(post.isPresent()) {
                //Si existe, devolvemos el ID
                return post.get().id();
            } else {
                //Si no existe, asumimos que es el primer post y devolvemos 1
                return "1";
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Post> findAll() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        return executeQuery(db.collection(COLLECTION).orderBy("createdAt", Query.Direction.DESCENDING));
    }

    public int like(Post post, String idUser) {
        int res = 0;
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference doc = db.collection(COLLECTION).document(post.id());
            ApiFuture<WriteResult> future = doc.update("likedBy", post.likedBy());
            future.get();
            return res;
        } catch (Exception e) {
            res = -1;
            return res;
        }
    }

    private List<Post> executeQuery(Query query) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Post> posts = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            Post post = doc.toObject(Post.class);
            posts.add(post);
        }
        return posts;
    }
}
