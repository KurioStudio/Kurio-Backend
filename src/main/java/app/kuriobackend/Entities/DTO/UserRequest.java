package app.kuriobackend.Entities.DTO;

import com.google.firebase.database.annotations.NotNull;

public record UserRequest(@NotNull String username, @NotNull String email, @NotNull String avatarImg, @NotNull String password) {

}
