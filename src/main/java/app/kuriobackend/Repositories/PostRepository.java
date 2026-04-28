package app.kuriobackend.Repositories;

import app.kuriobackend.Entities.Model.Post;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public class PostRepository {

    private final String COLLECTION = "posts";
    private final String FOLLOW_COLLECTION = "follow";

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public int guardarPost(Post post, List<MultipartFile> imagenes, MultipartFile file) {
        int res = 0;
        try {
            //Recogemos el ultimo ID de la base de datos
            String id = String.valueOf(Integer.parseInt(getUltimoId()) + 1);

            //Guardamos el archivo en MongoDB y recuperamos el oid
            String oid = guardarArchivoMongo(file);

            //Guardamos las imagenes en Firebase Storage y recuperamos las rutas
            ArrayList<String> rutasImg = guardarImagenes(imagenes);

            if(!oid.equals("-1")){
                //Creamos otro objeto Post para añadirle el id
                Post firebasePost = new Post(
                        id,
                        post.titulo(),
                        post.descripcion(),
                        rutasImg,
                        null,
                        null,
                        post.user(),
                        oid,
                        post.licencia(),
                        post.createdAt()
                );

                //Insertamos el nuevo post
                Firestore db = FirestoreClient.getFirestore();
                db.collection(COLLECTION).document(id).set(firebasePost).get();
            } else {
                res = -1;
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            res = 1;
            return res;
        }
    }

    private ArrayList<String> guardarImagenes(List<MultipartFile> imagenes) {
        ArrayList<String> urls = new ArrayList<>();
        try {
            Storage storage = StorageClient.getInstance().bucket().getStorage();
            String bucket = "kurio-6ecc0.firebasestorage.app";

            if(imagenes != null) {
                for (MultipartFile file : imagenes) {
                    if(!file.isEmpty()) {
                        String fileName = "posts/" + UUID.randomUUID() + ".jpg";
                        String imagenUrl = "https://firebasestorage.googleapis.com/v0/b/"
                                + bucket + "/o/"
                                + URLEncoder.encode(fileName, "UTF-8")
                                .replace("+", "%20").replace("/", "%2F")
                                + "?alt=media";

                        urls.add(imagenUrl);

                        storage.create(
                                BlobInfo.newBuilder(bucket, fileName)
                                    .setContentType(file.getContentType())
                                    .build(), file.getBytes()
                        );
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return urls;
    }

    private String guardarArchivoMongo(MultipartFile file) {
        try {
            ObjectId objectId = gridFsTemplate.store(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType()
            );

            return objectId.toHexString();
        } catch (Exception e) {
            return "-1";
        }
    }

    public GridFsResource descargarArchivo(String oid) {
        GridFSFile file = gridFsTemplate.findOne(
                new org.springframework.data.mongodb.core.query.Query(Criteria.where("_id").is(oid))
        );

        return gridFsTemplate.getResource(file);
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

    public int like(String idPost, String idUser) {
        int res = 0;
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference doc = db.collection(COLLECTION).document(idPost);
            DocumentSnapshot snapshot = doc.get().get();

            //Recogemos la lista de likes de una publicacion
            List<String> likedBy = (List<String>) snapshot.get("likedBy");

            //Si la lista no es nula y contiene el id del usuario, se quita de la lista. Si no, se añade
            if(likedBy != null && likedBy.contains(idUser)) {
                ApiFuture<WriteResult> future = doc.update("likedBy", FieldValue.arrayRemove(idUser));
                future.get();
            } else {
                ApiFuture<WriteResult> future = doc.update("likedBy", FieldValue.arrayUnion(idUser));
                future.get();
            }

            return res;
        } catch (Exception e) {
            res = -1;
            return res;
        }
    }

    public List<Post> findAllByTitle(String title) {
        List<Post> posts = new ArrayList<>();
        try {
            Firestore db = FirestoreClient.getFirestore();
            Query query = db.collection(COLLECTION).whereEqualTo("titulo", title).orderBy("createdAt", Query.Direction.DESCENDING);
            return executeQuery(query);
        } catch (Exception e) {
            return posts;
        }
    }

    public List<Post> findFollowed(String idFollower) {
        List<Post> posts = new ArrayList<>();
        try {
            Firestore db = FirestoreClient.getFirestore();
            Query query = db.collection(FOLLOW_COLLECTION).whereEqualTo("idFollower", idFollower);
            return executeQuery(query);
        } catch (Exception e) {
            return posts;
        }
    }

    public List<Post> findTopPosts() {
        List<Post> posts = new ArrayList<>();
        try {
            Firestore db = FirestoreClient.getFirestore();
            Query query = db.collection(COLLECTION).orderBy("likedBy", Query.Direction.ASCENDING);
            return executeQuery(query);
        } catch (Exception e) {
            return posts;
        }
    }

    public List<Post> findRecentPosts() {
        List<Post> posts = new ArrayList<>();
        try {
            Firestore db = FirestoreClient.getFirestore();
            Query query = db.collection(COLLECTION).orderBy("createdAt", Query.Direction.DESCENDING);
            return executeQuery(query);
        } catch (Exception e) {
            return posts;
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
