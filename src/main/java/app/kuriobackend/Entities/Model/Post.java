package app.kuriobackend.Entities.Model;

import app.kuriobackend.Entities.DTO.PostRequest;
import app.kuriobackend.Entities.DTO.PostResponse;

import java.time.LocalDate;
import java.util.ArrayList;

public record Post(String id, String titulo, String descripcion, ArrayList<String> imagenes, ArrayList<String> likedBy, Integer cantComentarios, User user, String oid, String licencia, String createdAt) {
    public static Post fromRequest(PostRequest pr) {
        return new Post(
                null,
                pr.titulo(),
                pr.descripcion(),
                pr.imagenes(),
                null,
                null,
                pr.user(),
                pr.oid(),
                pr.licencia(),
                LocalDate.now().toString()
        );
    }

    public PostResponse toResponse() {
        return new PostResponse(
                this.id,
                this.titulo,
                this.descripcion,
                this.imagenes,
                this.likedBy,
                this.cantComentarios,
                this.user,
                this.oid,
                this.licencia,
                this.createdAt
        );
    }
}
