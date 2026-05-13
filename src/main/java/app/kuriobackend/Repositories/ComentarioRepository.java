package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.Comentario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ComentarioRepository {

    private final String COLLECTION = "comentarios";
    private static final Logger logger = LogManager.getLogger(ComentarioRepository.class);

    public int guardar(Comentario comentario) {
        int res = 0;
        try{
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).add(comentario);
            return res;
        } catch (Exception e) {
            logger.error("Error al guardar comentario: {}", e.toString(), e);
            res = -1;
            return res;
        }
    }

    public List<Comentario> mostrarComentariosPost(String idPost) {
        try{
            Firestore db = FirestoreClient.getFirestore();
            Query query = db.collection(COLLECTION)
                    .whereEqualTo("idPost", idPost)
                    .orderBy("createdAt", Query.Direction.DESCENDING);
            return executeQuery(query);
        } catch (Exception e) {
            logger.error("Error al mostrar comentarios para postId='{}': {}", idPost, e.toString(), e);
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

    public int eliminarComentario(String idComentario) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference document = db.collection(COLLECTION).document(idComentario);
            DocumentSnapshot snapshot = document.get().get();

            if (snapshot.exists()) {
                document.delete().get();
                return 0;
            }

            Query query = db.collection(COLLECTION).whereEqualTo("id", idComentario).limit(1);
            QuerySnapshot querySnapshot = query.get().get();

            if (querySnapshot.isEmpty()) {
                logger.warn("No se encontró comentario para eliminar con id='{}'", idComentario);
                return -1;
            }

            querySnapshot.getDocuments().getFirst().getReference().delete().get();
            return 0;
        } catch (Exception e) {
            logger.error("Error al eliminar comentario con id='{}': {}", idComentario, e.toString(), e);
            return -1;
        }
    }
}
