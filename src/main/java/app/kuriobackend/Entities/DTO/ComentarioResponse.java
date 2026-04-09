package app.kuriobackend.Entities.DTO;

public record ComentarioResponse(String id, String idPost, String idUser, String contenido, String createdAt) {
}
