package app.kuriobackend.Entities.DTO;

import app.kuriobackend.Entities.Model.User;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public record PostRequest(@NotNull String titulo, @NotNull String descripcion, @NotNull ArrayList<String> imagenes, @NotNull User user, @NotNull String oid, @NotNull String licencia) {}
