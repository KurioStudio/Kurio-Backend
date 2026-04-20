package app.kuriobackend.Entities.Model;

import app.kuriobackend.Entities.DTO.UserRequest;
import app.kuriobackend.Entities.DTO.UserResponse;

import java.time.LocalDate;

public record User(String id, String username, String email, String avatarImg, String createdAt) {
    public static User fromRequest(UserRequest userRequest) {
        return new User(
                null,
                userRequest.username(),
                userRequest.email(),
                null,
                LocalDate.now().toString()
        );
    }

    public UserResponse toResponse() {
        return new UserResponse(
                this.id,
                this.username,
                this.email,
                this.avatarImg()
        );
    }
}
