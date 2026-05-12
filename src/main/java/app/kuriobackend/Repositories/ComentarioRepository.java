package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.Comentario;
import app.kuriobackend.Services.PostService;
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
    private static final Logger logger = LogManager.getLogger(PostService.class);

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
        int res = 0;
        try{
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).document(idComentario).delete().get();
            return res;
        } catch (Exception e) {
            res = -1;
            logger.error("Error al eliminar comentario: " + e.toString(), e);
            return res;
        }
    }
}
