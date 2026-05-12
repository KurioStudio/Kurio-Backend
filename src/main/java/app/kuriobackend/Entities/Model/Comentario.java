package app.kuriobackend.Entities.Model;

import app.kuriobackend.Entities.DTO.ComentarioRequest;
import app.kuriobackend.Entities.DTO.ComentarioResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record Comentario(String id, String idPost, String idUser, String contenido, String createdAt) {
    public static Comentario fromRequest(ComentarioRequest cr) {
        return new Comentario(
                UUID.randomUUID().toString(),
                cr.idPost(),
                cr.idUser(),
                cr.contenido(),
                LocalDateTime.now().toString()
        );
    }

    public ComentarioResponse toResponse() {
        return new ComentarioResponse(
                this.id,
                this.idPost,
                this.idUser,
                this.contenido,
                this.createdAt
        );
    }
}
