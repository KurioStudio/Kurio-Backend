package app.kuriobackend.Entities.Model;

import app.kuriobackend.Entities.DTO.ComentarioRequest;
import app.kuriobackend.Entities.DTO.ComentarioResponse;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record Comentario(String id, String idPost, String idUser, String contenido, String createdAt) {
    public static Comentario fromRequest(ComentarioRequest cr) {
        return new Comentario(
                UUID.randomUUID().toString(),
                cr.idPost(),
                cr.idUser(),
                cr.contenido(),
                ZonedDateTime
                    .now(ZoneId.of("Europe/Madrid"))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
