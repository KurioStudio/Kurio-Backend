package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.Comentario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public class ComentarioRepository {

    private final String COLLECTION = "comentarios";

    public int guardar(Comentario comentario) {
        int res = 0;
        try{
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).add(comentario);
            return res;
        } catch (RuntimeException e) {
            res = -1;
            return res;
        }
    }

    public List<Comentario> mostrarComentariosPost(String idPost) {
        try{
            Firestore db = FirestoreClient.getFirestore();
            Query query = db.collection(COLLECTION)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .whereEqualTo("idPost", idPost);
            return executeQuery(query);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<Comentario> executeQuery(Query query) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Comentario> comentarios = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            Comentario comentario = doc.toObject(Comentario.class);
            comentarios.add(comentario);
        }
        return comentarios;
    }

}
