package app.kuriobackend.Entities.DTO;

import com.google.firebase.database.annotations.NotNull;

public record ComentarioRequest(@NotNull String idPost, @NotNull String idUser, @NotNull String contenido) {
}
