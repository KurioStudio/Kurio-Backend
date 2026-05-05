package app.kuriobackend.Entities.DTO;

import app.kuriobackend.Entities.Model.User;

public record UserResponse(String id, String username, String email, String avatarImg, String createdAt) {

}
